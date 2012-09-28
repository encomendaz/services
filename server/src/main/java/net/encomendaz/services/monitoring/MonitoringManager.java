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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.encomendaz.services.tracking.Tracking;
import net.encomendaz.services.tracking.TrackingManager;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
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

	@SuppressWarnings("unchecked")
	private synchronized static Map<String, Entity> getCache() {
		Map<String, Entity> cache;
		MemcacheService memcache = getMemcacheService();

		if (memcache.contains(MonitoringManager.class)) {
			cache = (Map<String, Entity>) memcache.get(MonitoringManager.class);

		} else {
			cache = createCache();
			memcache.put(MonitoringManager.class, cache);
		}

		return cache;
	}

	private static Map<String, Entity> createCache() {
		Map<String, Entity> cache = Collections.synchronizedMap(new HashMap<String, Entity>());
		Monitoring monitoring;

		for (Entity entity : findAllEntities()) {
			monitoring = parse(entity);
			cache.put(getKey(monitoring), entity);
		}

		return cache;
	}

	// private static void refreshCache() {
	// MemcacheService memcache = getMemcacheService();
	// memcache.delete(MonitoringManager.class);
	// memcache.put(MonitoringManager.class, getCache());
	// }

	private static String getKey(Monitoring monitoring) {
		return monitoring.getClientId() + "-" + monitoring.getTrackId().toUpperCase();
	}

	public static void insert(Monitoring monitoring) {
		Tracking tracking = TrackingManager.search(monitoring.getTrackId());
		monitoring.setHash(tracking.getHash());
		monitoring.setCreated(new Date());

		Entity entity = new Entity("Monitoring");
		setProperty(entity, "clientId", monitoring.getClientId());
		setProperty(entity, "trackId", monitoring.getTrackId());
		setProperty(entity, "created", monitoring.getCreated());
		setProperty(entity, "hash", monitoring.getHash());
		setProperty(entity, "label", monitoring.getLabel());

		Map<String, Entity> cache = getCache();

		synchronized (cache) {
			DatastoreService datastore = getDatastoreService();
			datastore.put(entity);

			String key = getKey(monitoring);
			cache.put(key, entity);

			MemcacheService memcache = getMemcacheService();
			memcache.delete(MonitoringManager.class);
			memcache.put(MonitoringManager.class, cache);
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
		Map<String, Entity> cache = getCache();

		synchronized (cache) {
			String key = getKey(monitoring);
			Entity entity = cache.get(key);

			boolean updated = false;
			updated |= setProperty(entity, "label", monitoring.getLabel());
			updated |= setProperty(entity, "created", monitoring.getCreated());
			updated |= setProperty(entity, "updated", monitoring.getUpdated());
			updated |= setProperty(entity, "hash", monitoring.getHash());

			if (updated) {
				DatastoreService datastore = getDatastoreService();
				datastore.put(entity);

				cache.remove(key);
				cache.put(key, entity);

				MemcacheService memcache = getMemcacheService();
				memcache.delete(MonitoringManager.class);
				memcache.put(MonitoringManager.class, cache);
			}
		}
	}

	public static void delete(Monitoring monitoring) {
		Map<String, Entity> cache = getCache();

		synchronized (cache) {
			String key = getKey(monitoring);
			Entity entity = cache.get(key);

			DatastoreService datastore = getDatastoreService();
			datastore.delete(entity.getKey());

			cache.remove(key);

			MemcacheService memcache = getMemcacheService();
			memcache.delete(MonitoringManager.class);
			memcache.put(MonitoringManager.class, cache);
		}
	}

	public static Monitoring load(String clientId, String trackId) {
		String key = getKey(new Monitoring(clientId, trackId));
		Entity entity = getCache().get(key);

		return entity == null ? null : parse(entity);
	}

	public static List<Monitoring> findAll() {
		List<Monitoring> result = new ArrayList<Monitoring>();

		for (Entity entity : getCache().values()) {
			result.add(parse(entity));
		}

		return result;
	}

	private static List<Entity> findAllEntities() {
		List<Entity> result = Collections.synchronizedList(new ArrayList<Entity>());

		Query query = new Query("Monitoring");

		DatastoreService datastore = getDatastoreService();
		PreparedQuery preparedQuery = datastore.prepare(query);

		for (Entity entity : preparedQuery.asIterable()) {
			result.add(entity);
		}

		return result;
	}

	private static Monitoring parse(Entity entity) {
		String clientId = (String) entity.getProperty("clientId");
		String trackId = (String) entity.getProperty("trackId");
		Monitoring monitoring = new Monitoring(clientId, trackId);

		monitoring.setLabel((String) entity.getProperty("label"));
		monitoring.setCreated((Date) entity.getProperty("created"));
		monitoring.setUpdated((Date) entity.getProperty("updated"));
		monitoring.setHash((String) entity.getProperty("hash"));

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
