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
import java.util.Date;
import java.util.List;

import net.encomendaz.services.tracking.Tracking;
import net.encomendaz.services.tracking.TrackingManager;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

public class MonitoringManager {

	private static DatastoreService getDatastoreService() {
		return DatastoreServiceFactory.getDatastoreService();
	}

	private static MemcacheService getMemcacheService() {
		return MemcacheServiceFactory.getMemcacheService();
	}

	private static String getKey(Monitoring monitoring) {
		return monitoring.getId().toString();
	}

	public static void insert(Monitoring monitoring) {
		Tracking tracking = TrackingManager.search(monitoring.getTrackId());
		monitoring.setHash(tracking.getHash());
		monitoring.setCreated(new Date());

		String key = getKey(monitoring);

		Entity entity = new Entity("Monitoring", key);
		setProperty(entity, "clientId", monitoring.getClientId());
		setProperty(entity, "trackId", monitoring.getTrackId());
		setProperty(entity, "created", monitoring.getCreated());
		setProperty(entity, "hash", monitoring.getHash());
		setProperty(entity, "label", monitoring.getLabel());

		DatastoreService datastore = getDatastoreService();
		datastore.put(entity);

		MemcacheService memcache = getMemcacheService();
		memcache.put(key, entity);
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
		MemcacheService memcache = getMemcacheService();
		String key = getKey(monitoring);

		Entity entity;
		if (memcache.contains(key)) {
			entity = (Entity) memcache.get(key);

		} else {
			entity = loadFromDatastore(key);
		}

		boolean updated = false;
		updated |= setProperty(entity, "label", monitoring.getLabel());
		updated |= setProperty(entity, "created", monitoring.getCreated());
		updated |= setProperty(entity, "updated", monitoring.getUpdated());
		updated |= setProperty(entity, "hash", monitoring.getHash());

		if (updated) {
			DatastoreService datastore = getDatastoreService();
			datastore.put(entity);

			memcache.delete(key);
			memcache.put(entity, key);
		}
	}

	public static void delete(Monitoring monitoring) {
		MemcacheService memcache = getMemcacheService();
		String key = getKey(monitoring);

		Entity entity;
		if (memcache.contains(key)) {
			entity = (Entity) memcache.get(key);
			memcache.delete(key);

		} else {
			entity = loadFromDatastore(key);
		}

		DatastoreService datastore = getDatastoreService();
		datastore.delete(entity.getKey());
	}

	public static Monitoring load(String clientId, String trackId) {
		MemcacheService memcache = getMemcacheService();
		String key = getKey(new Monitoring(clientId, trackId));
		boolean cached = memcache.contains(key);

		Entity entity;
		if (cached) {
			entity = (Entity) memcache.get(key);
		} else {
			entity = loadFromDatastore(key);
		}

		Monitoring result = null;

		if (entity != null) {
			result = parse(entity);

			if (!cached) {
				memcache.put(getKey(result), entity);
			}
		}

		return result;
	}

	public static Entity loadFromDatastore(String key) {
		Entity result;

		try {
			DatastoreService datastore = getDatastoreService();
			result = datastore.get(KeyFactory.stringToKey(key));

		} catch (Exception cause) {
			result = null;
		}

		return result;
	}

	public static List<Monitoring> findAll() {
		List<Monitoring> result = new ArrayList<Monitoring>();

		Query query = new Query("Monitoring");

		DatastoreService datastore = getDatastoreService();
		PreparedQuery preparedQuery = datastore.prepare(query);

		for (Entity entity : preparedQuery.asIterable()) {
			result.add(parse(entity));
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

		Query query = new Query("Monitoring");
		query.setFilter(new FilterPredicate("clientId", Query.FilterOperator.EQUAL, clientId));

		DatastoreService datastore = getDatastoreService();
		PreparedQuery preparedQuery = datastore.prepare(query);

		for (Entity entity : preparedQuery.asIterable()) {
			result.add(parse(entity));
		}

		return result;
	}
}
