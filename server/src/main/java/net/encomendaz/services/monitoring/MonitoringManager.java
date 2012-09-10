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
import java.util.Map;
import java.util.TreeMap;

import net.encomendaz.services.tracking.Tracking;
import net.encomendaz.services.tracking.TrackingManager;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Query;

public class MonitoringManager {

	private static Map<String, Monitoring> cache;

	static {
		ObjectifyService.register(Monitoring.class);
	}

	private static Map<String, Monitoring> getCache() {
		if (cache == null) {
			loadCache();
		}

		return cache;
	}

	private static void loadCache() {
		Objectify objectify = ObjectifyService.begin();
		Query<Monitoring> query = objectify.query(Monitoring.class);

		cache = new TreeMap<String, Monitoring>();

		for (Monitoring monitoring : query.list()) {
			cache.put(getKey(monitoring), monitoring);
		}
	}

	private static String getKey(Monitoring monitoring) {
		return monitoring.getClientId() + "-" + monitoring.getTrackId().toUpperCase();
	}

	public static void insert(Monitoring monitoring) {
		Tracking tracking = TrackingManager.search(monitoring.getTrackId());

		monitoring.setHash(tracking.getHash());
		monitoring.setCreated(new Date());

		Objectify objectify = ObjectifyService.begin();
		objectify.put(monitoring);

		getCache().put(getKey(monitoring), monitoring);
	}

	public static void update(Monitoring monitoring) {
		Objectify objectify = ObjectifyService.begin();
		objectify.put(monitoring);

		String key = getKey(monitoring);
		getCache().remove(key);
		getCache().put(key, monitoring);
	}

	public static void delete(Monitoring monitoring) {
		Objectify objectify = ObjectifyService.begin();
		objectify.delete(monitoring);

		String key = getKey(monitoring);
		getCache().remove(key);
	}

	public static Monitoring load(String clientId, String trackId) {
		String key = getKey(new Monitoring(clientId, trackId));
		return getCache().get(key);
	}

	public static List<Monitoring> findAll() {
		List<Monitoring> result = new ArrayList<Monitoring>();

		for (Monitoring monitoring : getCache().values()) {
			result.add((Monitoring) monitoring.clone());
		}

		return result;
	}

	public static List<Monitoring> find(String clientId) {
		List<Monitoring> result = new ArrayList<Monitoring>();

		for (Monitoring monitoring : getCache().values()) {
			if (monitoring.getClientId().equals(clientId)) {
				result.add((Monitoring) monitoring.clone());
			}
		}

		return result;
	}
}
