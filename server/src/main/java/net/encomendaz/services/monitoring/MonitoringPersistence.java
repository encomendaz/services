/*
 * EncomendaZ
 * 
 * Copyright (c) 2011, EncomendaZ <http://encomendaz.net>.
 * All rights reserved.
 * 
 * EncomendaZ is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation; either version 3 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, see <http://gnu.org/licenses>
 * or write to the Free Software Foundation, Inc., 51 Franklin Street,
 * Fifth Floor, Boston, MA  02110-1301, USA.
 */
package net.encomendaz.services.monitoring;

import static com.google.appengine.api.taskqueue.TaskOptions.Method.DELETE;
import static com.google.appengine.api.taskqueue.TaskOptions.Method.POST;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import net.encomendaz.services.EncomendaZException;
import net.encomendaz.services.tracking.Tracking;
import net.encomendaz.services.tracking.TrackingManager;
import net.encomendaz.services.util.Strings;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.appengine.api.taskqueue.TaskOptions.Builder;

@Path("/admin/monitoring/cache")
public class MonitoringPersistence {

	private static DatastoreService getDatastoreService() {
		return DatastoreServiceFactory.getDatastoreService();
	}

	private static MemcacheService getMemcacheService() {
		return MemcacheServiceFactory.getMemcacheService();
	}

	private static String getKind() {
		return Monitoring.class.getSimpleName() + "_New";
	}

	public static void insert(Monitoring monitoring) throws EncomendaZException {
		before();
		Tracking tracking = TrackingManager.search(monitoring.getTrackId());

		if (tracking.isCompleted()) {
			throw new MonitoringException("O rastreamento " + tracking.getId() + " já foi finalizado");

		} else {
			monitoring.setHash(tracking.getHash());
			monitoring.setCreated(new Date());

			String clientId = monitoring.getClientId();
			String trackId = monitoring.getTrackId();

			String id = getId(clientId, trackId);
			Key key = getKey(id);

			Entity entity = new Entity(key);
			setProperty(entity, "clientId", clientId);
			setProperty(entity, "trackId", trackId);
			setProperty(entity, "created", monitoring.getCreated());
			setProperty(entity, "hash", monitoring.getHash());
			setProperty(entity, "label", monitoring.getLabel());

			getDatastoreService().put(entity);
			getMemcacheService().put(id, monitoring);

			addToCacheQueue(id);
		}
	}

	private static boolean setProperty(Entity entity, String property, Object value) {
		boolean updated = false;

		Object currentValue = getProperty(entity, property);

		if (value == null && currentValue != null) {
			entity.removeProperty(property);
			updated = true;

		} else if (value != null && !value.equals(currentValue)) {
			entity.setProperty(property, value);
			updated = true;
		}

		return updated;
	}

	public static void update(Monitoring monitoring) {
		before();

		String clientId = monitoring.getClientId();
		String trackId = monitoring.getTrackId();
		String id = getId(clientId, trackId);

		Entity entity = loadFromDatastore(id);

		boolean updated = false;
		updated |= setProperty(entity, "label", monitoring.getLabel());
		updated |= setProperty(entity, "unread", monitoring.isUnread());
		updated |= setProperty(entity, "created", monitoring.getCreated());
		updated |= setProperty(entity, "updated", monitoring.getUpdated());
		updated |= setProperty(entity, "completed", monitoring.getCompleted());
		updated |= setProperty(entity, "hash", monitoring.getHash());

		if (updated) {
			getDatastoreService().put(entity);

			getMemcacheService().delete(id);
			getMemcacheService().put(id, monitoring);
		}
	}

	public static void delete(Monitoring monitoring) {
		before();
		String id = getId(monitoring);

		getDatastoreService().delete(getKey(id));
		getMemcacheService().delete(id);

		removeFromCacheQueue(id);
	}

	private static void before() {
		final String key = "cache_status";

		if (Strings.isEmpty((String) getMemcacheService().get(key))) {
			getMemcacheService().put(key, "loading");
			// loadCacheQueue();
			loadCache();
		}

		while (!getMemcacheService().get(key).equals("loaded")) {
			try {
				Thread.sleep(5000);

			} catch (InterruptedException cause) {
				throw new RuntimeException(cause);
			}
		}
	}

	// private static void loadCacheQueue() {
	// TaskOptions taskOptions;
	// taskOptions = Builder.withUrl("/admin/monitoring/cache");
	// taskOptions.method(PUT);
	//
	// Queue queue = QueueFactory.getQueue("monitoring-cache");
	// queue.add(taskOptions);
	// }

	// @PUT
	private static void loadCache() {
		for (String id : findAllIdsFromDatastore()) {
			// addToCache(id);
			addToCacheQueue(id);
		}

		addToCacheQueue(null);

		//
		// final String key = "cache_status";
		// getMemcacheService().put(key, "loaded");
	}

	public static Set<String> getClientIds() {
		before();

		return getCachedClientIds();
	}

	@SuppressWarnings("unchecked")
	private static Set<String> getCachedClientIds() {
		Set<String> result = (Set<String>) getMemcacheService().get(getKind());

		if (result == null) {
			result = Collections.synchronizedSet(new HashSet<String>());
		}

		return result;
	}

	@SuppressWarnings("unchecked")
	private static Set<String> getCachedTrackIds(String clientId) {
		Set<String> result = null;

		if (!Strings.isEmpty(clientId)) {
			result = (Set<String>) getMemcacheService().get(clientId);
		}

		if (result == null) {
			result = Collections.synchronizedSet(new HashSet<String>());
		}

		return result;
	}

	private static Monitoring load(String id) {
		before();
		return (Monitoring) getMemcacheService().get(id);
	}

	public static Monitoring load(String clientId, String trackId) {
		before();
		return load(getId(clientId, trackId));

		//
		// Monitoring result = null;
		//
		// if (getCachedTrackIds(clientId).contains(trackId)) {
		// String id = getId(clientId, trackId);
		// result = (Monitoring) getMemcacheService().get(id);
		// }
		//
		// if (result == null) {
		// Entity entity = loadFromDatastore(clientId, trackId);
		// result = parse(entity);
		//
		// putOnCache(getId(result));
		// }
		//
		// return result;
	}

	// private static Entity loadFromDatastore(String clientId, String trackId) {
	// Query query = new Query(getKind());
	//
	// Filter filter = CompositeFilterOperator.and(new FilterPredicate("clientId", Query.FilterOperator.EQUAL,
	// clientId), new FilterPredicate("trackId", Query.FilterOperator.EQUAL, trackId));
	// query.setFilter(filter);
	//
	// PreparedQuery preparedQuery = getDatastoreService().prepare(query);
	// return preparedQuery.asSingleEntity();

	// return loadFromDatastore(getId(clientId, trackId));
	// }

	private static Entity loadFromDatastore(String id) {
		Entity entity;

		try {
			entity = getDatastoreService().get(getKey(id));
		} catch (EntityNotFoundException cause) {
			entity = null;
		}

		return entity;
	}

	private static String getId(Entity entity) {
		return entity.getKey().getName();
	}

	private static String getId(String clientId, String trackId) {
		return Monitoring.generateId(clientId, trackId);
	}

	private static String getId(Monitoring monitoring) {
		return getId(monitoring.getClientId(), monitoring.getTrackId());
	}

	private static Key getKey(String id) {
		return KeyFactory.createKey(getKind(), id);
	}

	private static void addToCacheQueue(String id) {
		TaskOptions taskOptions;
		taskOptions = Builder.withUrl("/admin/monitoring/cache");
		taskOptions.method(POST);

		if (!Strings.isEmpty(id)) {
			taskOptions = taskOptions.param("id", id);
		}

		Queue queue = QueueFactory.getQueue("monitoring-cache");
		queue.add(taskOptions);
	}

	@POST
	public static void addToCache(@FormParam("id") String id) {
		final String key = "cache_status";

		if (Strings.isEmpty(id)) {
			getMemcacheService().put(key, "loaded");

		} else {
			Entity entity = loadFromDatastore(id);
			Monitoring monitoring = parse(entity);

			Set<String> trackIds = getCachedTrackIds(monitoring.getClientId());
			trackIds.add(monitoring.getTrackId());

			Set<String> clientIds = getCachedClientIds();
			clientIds.add(monitoring.getClientId());

			getMemcacheService().put(getKind(), clientIds);
			getMemcacheService().put(monitoring.getClientId(), trackIds);
			getMemcacheService().put(id, monitoring);
		}
	}

	private static void removeFromCacheQueue(String id) {
		TaskOptions taskOptions;
		taskOptions = Builder.withUrl("/admin/monitoring/cache");
		taskOptions = taskOptions.param("id", id);
		taskOptions.method(DELETE);

		Queue queue = QueueFactory.getQueue("monitoring-cache");
		queue.add(taskOptions);
	}

	// @GET
	// @Path("/xx")
	// public static void xxx() {
	// Query query = new Query(getKind());
	// PreparedQuery preparedQuery = getDatastoreService().prepare(query);
	// query.setFilter(new FilterPredicate("clientId", Query.FilterOperator.EQUAL, "91448300404063076307502904506675"));
	//
	// for (Entity entity : preparedQuery.asIterable()) {
	// delete(parse(entity));
	// }
	// }

	@DELETE
	public static void removeFromCache(@QueryParam("id") String id) {
		Monitoring monitoring = parse(loadFromDatastore(id));

		if (monitoring != null) {
			String clientId = monitoring.getClientId();
			String trackId = monitoring.getTrackId();

			Set<String> trackIds = getCachedTrackIds(clientId);
			trackIds.remove(trackId);

			if (trackIds.isEmpty()) {
				Set<String> clientIds = getCachedClientIds();
				clientIds.remove(clientId);

				getMemcacheService().delete(clientId);
				getMemcacheService().put(getKind(), clientIds);

			} else {
				getMemcacheService().put(clientId, trackIds);
			}

			getMemcacheService().delete(getId(monitoring));
		}
	}

	public static List<Monitoring> findAll() {
		before();
		List<Monitoring> result = new ArrayList<Monitoring>();

		for (String clientId : getCachedClientIds()) {
			result.addAll(find(clientId));
		}

		return result;

		// Set<String> clientIds = getCachedClientIds();
		//
		// for () {
		//
		// }

		// if (clientIds.isEmpty()) {
		// Monitoring monitoring;
		//
		// for (Entity entity : findAllFromDatastore()) {
		// // TODO Reaproveitar o load!
		// monitoring = parse(entity);
		// result.add(monitoring);
		//
		// putOnCache(getId(monitoring));
		// }
		//
		// } else {
		// for (String clientId : clientIds) {
		// result.addAll(find(clientId));
		// }
		// }

		// return result;
	}

	private static List<String> findAllIdsFromDatastore() {
		Query query = new Query(getKind()).setKeysOnly();
		PreparedQuery preparedQuery = getDatastoreService().prepare(query);

		List<String> ids = new ArrayList<String>();

		for (Entity entity : preparedQuery.asIterable()) {
			ids.add(getId(entity));
		}

		return ids;
	}

	// public static List<String> findAllIdsFromDatastore() {
	// Query query = new Query(getKind()).setKeysOnly();
	// PreparedQuery preparedQuery = getDatastoreService().prepare(query);
	//
	// List<String> ids = Collections.synchronizedList(new ArrayList<String>());
	//
	// for (Entity entity : preparedQuery.asIterable()) {
	// ids.add(entity.getKey().getName());
	// }
	//
	// return ids;
	// }

	public static List<Monitoring> find(String clientId) {
		before();
		List<Monitoring> result = new ArrayList<Monitoring>();
		Monitoring monitoring;

		for (String trackId : getCachedTrackIds(clientId)) {
			monitoring = load(clientId, trackId);

			if (monitoring != null) {
				result.add(load(clientId, trackId));
			}
		}

		return result;
	}

	private static Monitoring parse(Entity entity) {
		Monitoring monitoring = null;

		if (entity != null) {
			String clientId = getProperty(entity, "clientId");
			String trackId = getProperty(entity, "trackId");

			monitoring = new Monitoring(clientId, trackId);
			monitoring.setLabel((String) getProperty(entity, "label"));
			monitoring.setUnread((Boolean) getProperty(entity, "unread"));
			monitoring.setCreated((Date) getProperty(entity, "created"));
			monitoring.setUpdated((Date) getProperty(entity, "updated"));
			monitoring.setCompleted((Date) getProperty(entity, "completed"));
			monitoring.setHash((String) getProperty(entity, "hash"));
		}

		return monitoring;
	}

	private static <T> T getProperty(Entity entity, String property) {
		@SuppressWarnings("unchecked")
		T result = (T) entity.getProperty(property);

		if (result instanceof String && Strings.isEmpty((String) result)) {
			result = null;
		}

		return result;
	}
}
