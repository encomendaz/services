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

import static com.google.appengine.api.datastore.FetchOptions.Builder.withChunkSize;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

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
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

public class MonitoringManager {

	private static DatastoreService getDatastoreService() {
		return DatastoreServiceFactory.getDatastoreService();
	}

	private static MemcacheService getMemcacheService() {
		return MemcacheServiceFactory.getMemcacheService();
	}

	// @SuppressWarnings("unchecked")
	// private synchronized static Map<Long, Monitoring> getCache() {
	// Map<Long, Monitoring> cache;
	// MemcacheService memcache = getMemcacheService();
	//
	// if (memcache.contains(MonitoringManager.class)) {
	// cache = (Map<Long, Monitoring>) memcache.get(MonitoringManager.class);
	//
	// } else {
	// cache = Collections.synchronizedMap(new HashMap<Long, Monitoring>());
	// memcache.put(MonitoringManager.class, cache);
	// }
	//
	// return cache;
	// }

	// private static Map<Long, Monitoring> createCache() {
	// Map<Long, Monitoring> cache = Collections.synchronizedMap(new HashMap<Long, Monitoring>());
	// Monitoring monitoring;
	//
	// for (Entity entity : findAllEntities()) {
	// monitoring = parse(entity);
	// cache.put(generateId(monitoring), entity);
	// }
	//
	// return cache;
	// }

	// private static void refreshCache() {
	// MemcacheService memcache = getMemcacheService();
	// memcache.delete(MonitoringManager.class);
	// memcache.put(MonitoringManager.class, getCache());
	// }

	// private static Long createId(Monitoring monitoring) {
	// return createId(monitoring.getClientId(), monitoring.getTrackId());
	// }

	private static String createId(String clientId, String trackId) {
		return Monitoring.generateId(clientId, trackId);
	}

	private static Key createKey(String id) {
		return KeyFactory.createKey(getKind(), id);
	}

	private static String getKind() {
		return Monitoring.class.getSimpleName();
	}

	public synchronized static void insert(Monitoring monitoring) throws MonitoringException {
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

	public synchronized static void update(Monitoring monitoring) {
		String clientId = monitoring.getClientId();
		String trackId = monitoring.getTrackId();
		String id = createId(clientId, trackId);

		Entity entity = loadFromDatastore(id);

		boolean updated = false;
		updated |= setProperty(entity, "label", monitoring.getLabel());
		updated |= setProperty(entity, "created", monitoring.getCreated());
		updated |= setProperty(entity, "updated", monitoring.getUpdated());
		updated |= setProperty(entity, "hash", monitoring.getHash());

		if (updated) {
			getDatastoreService().put(entity);

			getMemcacheService().delete(id);
			getMemcacheService().put(id, monitoring);
		}
	}

	public synchronized static void delete(Monitoring monitoring) {
		String id = createId(monitoring.getClientId(), monitoring.getTrackId());
		Key key = createKey(id);

		getDatastoreService().delete(key);
		getMemcacheService().delete(id);
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

	public static Monitoring load(String clientId, String trackId) {
		return load(Monitoring.generateId(clientId, trackId));
	}

	public static Monitoring load(String id) {
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

	// public static Monitoring load(String clientId, String trackId) {
	// Long id = createId(clientId, trackId);
	// Monitoring monitoring = (Monitoring) getMemcacheService().get(id);
	//
	// if (monitoring == null) {
	// Entity entity = loadFromDatastore(clientId, trackId);
	//
	// if (entity != null) {
	// monitoring = parse(entity);
	// getMemcacheService().put(id, monitoring);
	// }
	// }
	//
	// return monitoring;
	// }

	public synchronized static List<String> findIds() {
		List<String> result = Collections.synchronizedList(new ArrayList<String>());
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

		Query query = new Query("Monitoring").setKeysOnly();
		PreparedQuery preparedQuery = datastore.prepare(query);
		Key key;

		for (Entity entity : preparedQuery.asIterable(withChunkSize(20000))) {
			key = entity.getKey();
			result.add(KeyFactory.keyToString(key));
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
			monitoring.setCreated((Date) entity.getProperty("created"));
			monitoring.setUpdated((Date) entity.getProperty("updated"));
			monitoring.setHash((String) entity.getProperty("hash"));
		}

		return monitoring;
	}

	// public synchronized static List<Monitoring> find(String clientId) {
	// List<Monitoring> result = new ArrayList<Monitoring>();
	//
	// for (Monitoring monitoring : findAll()) {
	// if (clientId != null && clientId.equals(monitoring.getClientId())) {
	// result.add(monitoring);
	// }
	// }
	//
	// return result;
	// }
}
