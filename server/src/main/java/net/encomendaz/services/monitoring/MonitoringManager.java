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
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
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

	private static String getCacheKey(Monitoring monitoring) {
		return monitoring.getClientId() + "-" + monitoring.getTrackId().toUpperCase();
	}

	public static void insert(Monitoring monitoring) {
		Tracking tracking = TrackingManager.search(monitoring.getTrackId());
		monitoring.setHash(tracking.getHash());
		monitoring.setCreated(new Date());

		Entity entity = new Entity("Monitoring");
		entity.setProperty("clientId", monitoring.getClientId());
		entity.setProperty("trackId", monitoring.getTrackId());
		entity.setProperty("created", monitoring.getCreated());
		entity.setProperty("hash", monitoring.getHash());

		if (monitoring.getLabel() != null) {
			entity.setProperty("label", monitoring.getLabel());
		}

		DatastoreService datastore = getDatastoreService();
		datastore.put(entity);

		MemcacheService memcache = getMemcacheService();
		String cacheKey = getCacheKey(monitoring);
		memcache.put(cacheKey, entity);
	}

	public static void update(Monitoring monitoring) {
		MemcacheService memcache = getMemcacheService();
		String cacheKey = getCacheKey(monitoring);

		Entity entity;
		if (memcache.contains(cacheKey)) {
			entity = (Entity) memcache.get(cacheKey);

		} else {
			entity = loadFromDatastore(cacheKey, cacheKey);
		}

		boolean changed = false;

		String label = (String) entity.getProperty("label");
		if (label != null && !label.equals(monitoring.getLabel())) {
			if (monitoring.getLabel() == null) {
			} else {
				entity.setProperty("label", monitoring.getLabel());
			}

			changed = true;
		}

		Date created = (Date) entity.getProperty("created");
		if (created != null && !created.equals(monitoring.getCreated())) {
			if (monitoring.getCreated() == null) {
				entity.removeProperty("created");
			} else {
				entity.setProperty("created", monitoring.getCreated());
			}

			changed = true;
		}

		Date updated = (Date) entity.getProperty("updated");
		if (updated != null && !updated.equals(monitoring.getUpdated())) {
			if (monitoring.getUpdated() == null) {
				entity.removeProperty("updated");
			} else {
				entity.setProperty("updated", monitoring.getUpdated());
			}

			changed = true;
		}

		String hash = (String) entity.getProperty("hash");
		if (hash != null && !hash.equals(monitoring.getHash())) {
			if (monitoring.getHash() == null) {
				entity.removeProperty("hash");
			} else {
				entity.setProperty("hash", monitoring.getHash());
			}

			changed = true;
		}

		if (changed) {
			DatastoreService datastore = getDatastoreService();
			datastore.put(entity);

			memcache.delete(cacheKey);
			memcache.put(entity, cacheKey);
		}
	}

	public static void delete(Monitoring monitoring) {
		MemcacheService memcache = getMemcacheService();
		String cacheKey = getCacheKey(monitoring);

		Entity entity;
		if (memcache.contains(cacheKey)) {
			entity = (Entity) memcache.get(cacheKey);
			memcache.delete(cacheKey);

		} else {
			entity = loadFromDatastore(cacheKey, cacheKey);
		}

		DatastoreService datastore = getDatastoreService();
		datastore.delete(entity.getKey());
	}

	public static Monitoring load(String clientId, String trackId) {
		MemcacheService memcache = getMemcacheService();
		String cacheKey = getCacheKey(new Monitoring(clientId, trackId));
		boolean cached = memcache.contains(cacheKey);

		Entity entity;
		if (cached) {
			entity = (Entity) memcache.get(cacheKey);

		} else {
			entity = loadFromDatastore(clientId, trackId);
		}

		Monitoring result = null;

		if (entity != null) {
			result = parse(entity);

			if (!cached) {
				memcache.put(getCacheKey(result), entity);
			}
		}

		return result;
	}

	public static Entity loadFromDatastore(String clientId, String trackId) {
		Query query = new Query("Monitoring");
		query.setFilter(CompositeFilterOperator.and(new FilterPredicate("clientId", Query.FilterOperator.EQUAL,
				clientId), new FilterPredicate("trackId", Query.FilterOperator.EQUAL, trackId)));

		DatastoreService datastore = getDatastoreService();
		PreparedQuery preparedQuery = datastore.prepare(query);

		return preparedQuery.asSingleEntity();
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
		Monitoring monitoring = new Monitoring();

		monitoring.setClientId((String) entity.getProperty("clientId"));
		monitoring.setTrackId((String) entity.getProperty("trackId"));
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
