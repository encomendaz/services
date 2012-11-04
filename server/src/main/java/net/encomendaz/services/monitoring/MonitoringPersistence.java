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
import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import net.encomendaz.services.EncomendaZException;
import net.encomendaz.services.tracking.Tracking;
import net.encomendaz.services.tracking.TrackingManager;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.appengine.api.taskqueue.TaskOptions.Builder;

@Path("/monitoring/cache")
public class MonitoringPersistence {

	private static DatastoreService getDatastoreService() {
		return DatastoreServiceFactory.getDatastoreService();
	}

	private static MemcacheService getMemcacheService() {
		return MemcacheServiceFactory.getMemcacheService();
	}

	private static String createId(String clientId, String trackId) {
		return Monitoring.generateId(clientId, trackId);
	}

	private static Key createKey(String id) {
		return KeyFactory.createKey(getKind(), id);
	}

	private static String getKind() {
		return Monitoring.class.getSimpleName() + "_New";
	}

	public static void insert(Monitoring monitoring) throws EncomendaZException {
		Tracking tracking = TrackingManager.search(monitoring.getTrackId());

		if (tracking.isCompleted()) {
			throw new MonitoringException("O rastreamento " + tracking.getId() + " já foi finalizado");

		} else {
			monitoring.setHash(tracking.getHash());
			monitoring.setCreated(new Date());

			String clientId = monitoring.getClientId();
			String trackId = monitoring.getTrackId();

			String id = createId(clientId, trackId);
			Key key = createKey(id);

			Entity entity = new Entity(key);
			setProperty(entity, "clientId", clientId);
			setProperty(entity, "trackId", trackId);
			setProperty(entity, "created", monitoring.getCreated());
			setProperty(entity, "hash", monitoring.getHash());
			setProperty(entity, "label", monitoring.getLabel());

			getDatastoreService().put(entity);
			getMemcacheService().put(id, monitoring);

			TaskOptions taskOptions;
			taskOptions = Builder.withUrl("/monitoring/cache");
			taskOptions = taskOptions.param("id", id);
			taskOptions.method(POST);

			Queue queue = QueueFactory.getQueue("monitoring-cache");
			queue.add(taskOptions);

		}
	}

	private static boolean setProperty(Entity entity, String property, Object value) {
		boolean updated = false;

		Object currentValue = entity.getProperty(property);

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
		String clientId = monitoring.getClientId();
		String trackId = monitoring.getTrackId();
		String id = createId(clientId, trackId);

		Entity entity = loadFromDatastore(clientId, trackId);

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
		String id = createId(monitoring.getClientId(), monitoring.getTrackId());
		Key key = createKey(id);

		getDatastoreService().delete(key);
		getMemcacheService().delete(id);

		TaskOptions taskOptions;
		taskOptions = Builder.withUrl("/monitoring/cache");
		taskOptions = taskOptions.param("id", id);
		taskOptions.method(DELETE);

		Queue queue = QueueFactory.getQueue("monitoring-cache");
		queue.add(taskOptions);
	}

	private static Entity loadFromDatastore(String clientId, String trackId) {
		Query query = new Query(getKind());
		query.setFilter(CompositeFilterOperator.and(new FilterPredicate("clientId", Query.FilterOperator.EQUAL,
				clientId), new FilterPredicate("trackId", Query.FilterOperator.EQUAL, trackId)));

		PreparedQuery preparedQuery = getDatastoreService().prepare(query);
		return preparedQuery.asSingleEntity();
	}

	public static Monitoring load(String clientId, String trackId) {
		return load(createId(clientId, trackId));
	}

	private static Monitoring load(String id) {
		Monitoring monitoring = (Monitoring) getMemcacheService().get(id);

		if (monitoring == null) {
			Entity entity = loadFromDatastore(id);

			if (entity != null) {
				monitoring = parse(entity);
				getMemcacheService().put(id, monitoring);
			}
		}

		return monitoring;
	}

	private static Entity loadFromDatastore(String id) {
		Entity entity;

		try {
			Key key = createKey(id);
			entity = getDatastoreService().get(key);

		} catch (EntityNotFoundException cause) {
			entity = null;
		}

		return entity;
	}

	private static List<String> getIds() {
		@SuppressWarnings("unchecked")
		List<String> ids = (List<String>) getMemcacheService().get(getKind());

		if (ids == null) {
			Query query = new Query(getKind()).setKeysOnly();
			PreparedQuery preparedQuery = getDatastoreService().prepare(query);

			ids = Collections.synchronizedList(new ArrayList<String>());
			// ids = new ArrayList<String>();

			for (Entity entity : preparedQuery.asIterable()) {
				ids.add(entity.getKey().getName());
			}

			getMemcacheService().put(getKind(), ids);
		}

		return ids;
	}

	@POST
	public static void addToCache(@FormParam("id") String id) {
		List<String> ids = getIds();
		ids.add(id);

		getMemcacheService().put(getKind(), ids);
	}

	@DELETE
	public static void removeFromCache(@QueryParam("id") String id) {
		List<String> ids = getIds();
		ids.remove(id);

		getMemcacheService().put(getKind(), ids);
	}

	public static List<Monitoring> findAll() {
		List<Monitoring> result = new ArrayList<Monitoring>();
		Monitoring monitoring;

		for (String id : getIds()) {
			monitoring = load(id);

			if (monitoring != null) {
				result.add(monitoring);
			}
		}

		return result;
	}

	private static Monitoring parse(Entity entity) {
		Monitoring monitoring = null;

		if (entity != null) {
			String clientId = (String) entity.getProperty("clientId");
			String trackId = (String) entity.getProperty("trackId");

			monitoring = new Monitoring(clientId, trackId);
			monitoring.setClientId((String) entity.getProperty("clientId"));
			monitoring.setTrackId((String) entity.getProperty("trackId"));
			monitoring.setLabel((String) entity.getProperty("label"));
			monitoring.setUnread((Boolean) entity.getProperty("unread"));
			monitoring.setCreated((Date) entity.getProperty("created"));
			monitoring.setUpdated((Date) entity.getProperty("updated"));
			monitoring.setCompleted((Date) entity.getProperty("completed"));
			monitoring.setHash((String) entity.getProperty("hash"));
		}

		return monitoring;
	}

	public static List<Monitoring> find(String clientId) {
		List<Monitoring> result = new ArrayList<Monitoring>();

		for (Monitoring monitoring : findAll()) {
			if (clientId != null && clientId.equals(monitoring.getClientId())) {
				result.add(monitoring);
			}
		}

		return result;
	}
}
