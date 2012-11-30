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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import com.google.appengine.api.datastore.PropertyProjection;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheService.IdentifiableValue;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

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
		Tracking tracking = TrackingManager.search(monitoring.getTrackId());

		if (tracking.isCompleted()) {
			throw new MonitoringException("Finalizado");

		} else {
			monitoring.setHash(tracking.getHash());
			monitoring.setCreated(new Date());

			String clientId = monitoring.getClientId();
			String trackId = monitoring.getTrackId();

			String id = generateId(clientId, trackId);
			Key key = getKey(id);

			Entity entity = new Entity(key);
			setProperty(entity, "clientId", clientId);
			setProperty(entity, "trackId", trackId);
			setProperty(entity, "created", monitoring.getCreated());
			setProperty(entity, "hash", monitoring.getHash());
			setProperty(entity, "label", monitoring.getLabel());

			getDatastoreService().put(entity);

			// addMonitoringToCache(monitoring);
			addMonitoringToCache(monitoring.getClientId(), monitoring);
			addClientIdToCache(monitoring.getClientId(), false);
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
		String clientId = monitoring.getClientId();
		String trackId = monitoring.getTrackId();
		String id = generateId(clientId, trackId);

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

			// addMonitoringToCache(monitoring);
			addMonitoringToCache(monitoring.getClientId(), monitoring);
			addClientIdToCache(monitoring.getClientId(), false);
		}
	}

	public static void delete(Monitoring monitoring) {
		String id = generateId(monitoring);
		getDatastoreService().delete(getKey(id));

		// removeMonitoringFromCache(monitoring.getClientId(), monitoring.getTrackId());
		removeMonitoringFromCache(monitoring.getClientId(), monitoring);
	}

	private static Set<String> getClientIds() {
		@SuppressWarnings("unchecked")
		Set<String> clientIds = (Set<String>) getMemcacheService().get(getKind());

		if (clientIds == null) {
			Query query = new Query(getKind());
			query.addProjection(new PropertyProjection("clientId", String.class));

			PreparedQuery preparedQuery = getDatastoreService().prepare(query);
			clientIds = Collections.synchronizedSet(new HashSet<String>());
			String clientId;

			for (Entity entity : preparedQuery.asIterable()) {
				clientId = getProperty(entity, "clientId");

				addClientIdToCache(clientId, true);
				clientIds.add(clientId);
			}
		}

		return clientIds;
	}

	private static Set<Monitoring> getMonitorings(String clientId) {
		@SuppressWarnings("unchecked")
		Set<Monitoring> result = (Set<Monitoring>) getMemcacheService().get(clientId);

		if (result == null) {
			Query query = new Query(getKind());
			// query.addProjection(new PropertyProjection("trackId", String.class));
			query.setFilter(new FilterPredicate("clientId", Query.FilterOperator.EQUAL, clientId));

			PreparedQuery preparedQuery = getDatastoreService().prepare(query);
			result = Collections.synchronizedSet(new HashSet<Monitoring>());
			Monitoring monitoring;

			for (Entity entity : preparedQuery.asIterable()) {
				monitoring = parse(entity);

				addMonitoringToCache(clientId, monitoring);
				result.add(monitoring);
			}
		}

		return result;
	}

	// private static void addMonitoringToCache(Monitoring monitoring) {
	// String id = getId(monitoring);
	// getMemcacheService().put(id, monitoring);
	// }

	// private static boolean removeMonitoringFromCache(String clientId, String trackId) {
	// String id = getId(clientId, trackId);
	// boolean deleted = getMemcacheService().delete(id);
	//
	// if (deleted) {
	// removeTrackIdFromCache(clientId, trackId);
	// }
	//
	// return deleted;
	// }

	@SuppressWarnings("unchecked")
	private static boolean addMonitoringToCache(String clientId, Monitoring monitoring) {
		boolean result = true;

		IdentifiableValue identifiable = getMemcacheService().getIdentifiable(clientId);
		Set<Monitoring> monitorings;

		if (identifiable != null) {
			monitorings = (Set<Monitoring>) identifiable.getValue();
			monitorings.add(monitoring);

			result = getMemcacheService().putIfUntouched(clientId, identifiable, monitorings);

		} else {
			monitorings = Collections.synchronizedSet(new HashSet<Monitoring>());
			monitorings.add(monitoring);

			getMemcacheService().put(clientId, monitorings);
		}

		if (!result) {
			addMonitoringToCache(clientId, monitoring);
		}

		return result;
	}

	@SuppressWarnings("unchecked")
	private static boolean removeMonitoringFromCache(String clientId, Monitoring monitoring) {
		boolean result = true;

		IdentifiableValue identifiable = getMemcacheService().getIdentifiable(clientId);
		List<Monitoring> monitorings;

		if (identifiable != null) {
			monitorings = (List<Monitoring>) identifiable.getValue();

			if (monitorings.remove(monitoring)) {
				result = getMemcacheService().putIfUntouched(clientId, identifiable, monitorings);
			}
		}

		if (!result) {
			removeMonitoringFromCache(clientId, monitoring);
		}

		return result;
	}

	// private static Expiration getCacheExpiration() {
	// return Expiration.byDeltaSeconds(Integer.MAX_VALUE);
	// }

	@SuppressWarnings("unchecked")
	private static boolean addClientIdToCache(String clientId, boolean force) {
		boolean result = true;

		IdentifiableValue identifiable = getMemcacheService().getIdentifiable(getKind());
		Set<String> clientIds;

		if (identifiable != null) {
			clientIds = (Set<String>) identifiable.getValue();
			clientIds.add(clientId);

			result = getMemcacheService().putIfUntouched(getKind(), identifiable, clientIds);

		} else if (force) {
			clientIds = Collections.synchronizedSet(new HashSet<String>());
			clientIds.add(clientId);

			getMemcacheService().put(getKind(), clientIds);
		}

		if (!result) {
			addClientIdToCache(clientId, force);
		}

		return result;
	}

	@SuppressWarnings("unchecked")
	private static boolean removeClientIdFromCache(String clientId) {
		boolean result = true;

		IdentifiableValue identifiable = getMemcacheService().getIdentifiable(getKind());
		Set<String> clientIds;

		if (identifiable != null) {
			clientIds = (Set<String>) identifiable.getValue();

			if (clientIds.remove(clientId)) {
				result = getMemcacheService().putIfUntouched(clientId, identifiable, clientIds);
			}
		}

		if (!result) {
			removeClientIdFromCache(clientId);
		}

		return result;
	}

	public static Monitoring load(String clientId, String trackId) {
		Monitoring result = null;
		Set<Monitoring> monitorings = getMonitorings(clientId);

		if (monitorings != null) {
			for (Monitoring monitoring : monitorings) {
				if (monitoring.getTrackId() != null && trackId != null && monitoring.getTrackId().equals(trackId)) {
					result = monitoring;
					break;
				}
			}
		}

		return result;
	}

	// public static Monitoring load(String clientId, String trackId) {
	// String id = getId(clientId, trackId);
	// Monitoring monitoring = getMonitoring(clientId, trackId);
	//
	// if (monitoring == null) {
	// Entity entity = loadFromDatastore(id);
	//
	// if (entity != null) {
	// monitoring = parse(entity);
	//
	// addMonitoringToCache(monitoring);
	// addTrackIdToCache(monitoring.getClientId(), monitoring);
	// addClientIdToCache(monitoring.getClientId(), false);
	// }
	// }
	//
	// return monitoring;
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

	private static String generateId(String clientId, String trackId) {
		return Monitoring.generateId(clientId, trackId);
	}

	private static String generateId(Monitoring monitoring) {
		return generateId(monitoring.getClientId(), monitoring.getTrackId());
	}

	private static Key getKey(String id) {
		return KeyFactory.createKey(getKind(), id);
	}

	public static List<Monitoring> findAll() {
		List<Monitoring> result = new ArrayList<Monitoring>();
		List<Monitoring> monitorings;

		for (String clientId : getClientIds()) {
			monitorings = find(clientId);

			if (monitorings == null || monitorings.isEmpty()) {
				removeClientIdFromCache(clientId);
				// removeMonitoringFromCache(clientId, monitoring);
				// removeTrackIdFromCache(clientId, trackId);
			} else {
				result.addAll(monitorings);
			}
		}

		return result;
	}

	public static List<Monitoring> find(String clientId) {
		List<Monitoring> result = new ArrayList<Monitoring>();

		for (Monitoring monitoring : getMonitorings(clientId)) {
			// monitoring = load(clientId, trackId);

			// if (monitoring == null) {
			// removeMonitoringFromCache(clientId, monitoring);
			// removeTrackIdFromCache(clientId, trackId);

			// } else {
			result.add(monitoring);
			// }
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
