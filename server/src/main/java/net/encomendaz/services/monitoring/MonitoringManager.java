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

import java.util.Date;
import java.util.List;

import net.encomendaz.services.tracking.Tracking;
import net.encomendaz.services.tracking.TrackingManager;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Query;

public class MonitoringManager {

	static {
		ObjectifyService.register(Monitoring.class);
	}

	public static void insert(Monitoring monitoring) {
		Tracking tracking = TrackingManager.search(monitoring.getTrackId());

		monitoring.setHash(tracking.getHash());
		monitoring.setCreated(new Date());

		Objectify objectify = ObjectifyService.begin();
		objectify.put(monitoring);
	}

	public static void update(Monitoring monitoring) {
		Objectify objectify = ObjectifyService.begin();
		objectify.put(monitoring);
	}
	
	public static void delete(Monitoring monitoring) {
		Objectify objectify = ObjectifyService.begin();
		objectify.delete(monitoring);
	}

	public static List<Monitoring> findAll() {
		Objectify objectify = ObjectifyService.begin();
		Query<Monitoring> query = objectify.query(Monitoring.class).order("clientId").order("trackId");

		return query.list();
	}
}
