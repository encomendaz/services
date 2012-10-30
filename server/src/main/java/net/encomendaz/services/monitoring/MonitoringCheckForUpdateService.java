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

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import net.encomendaz.services.notification.NotificationManager;
import net.encomendaz.services.tracking.Tracking;
import net.encomendaz.services.tracking.TrackingManager;

@Path("/monitoring/task/check-for-update")
public class MonitoringCheckForUpdateService {

	@GET
	public void execute(@QueryParam("clientId") String clientId, @QueryParam("trackId") String trackId)
			throws Exception {
		Monitoring monitoring = MonitoringPersistence.load(clientId, trackId);

		Date date = new Date();
		Tracking tracking = TrackingManager.search(monitoring.getTrackId());
		String hash = tracking.getHash();

		if (!monitoring.getHash().equals(hash)) {
			monitoring.setHash(hash);
			monitoring.setUpdated(date);

			MonitoringPersistence.update(monitoring);
			NotificationManager.send(monitoring, tracking);
		}

		if (tracking.isCompleted()) {
			MonitoringPersistence.delete(monitoring);
		} else {
			monitoring.setMonitored(date);
		}
	}
}
