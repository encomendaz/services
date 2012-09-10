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

import static net.encomendaz.services.Response.MEDIA_TYPE;
import static net.encomendaz.services.Response.Status.OK;

import java.util.Date;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import net.encomendaz.services.Response;
import net.encomendaz.services.notification.NotificationManager;
import net.encomendaz.services.tracking.Tracking;
import net.encomendaz.services.tracking.TrackingManager;

@Path("/monitoring.cron")
@Produces(MEDIA_TYPE)
public class MonitoringCron {

	@GET
	public Response<String> execute() throws Exception {
		String hash;
		Date date;
		Tracking tracking;
		Exception exception = null;

		for (Monitoring monitoring : MonitoringManager.findAll()) {
			try {
				date = new Date();
				tracking = TrackingManager.search(monitoring.getTrackId());
				hash = tracking.getHash();

				if (!monitoring.getHash().equals(hash)) {
					monitoring.setHash(hash);
					monitoring.setUpdated(date);

					MonitoringManager.update(monitoring);
					NotificationManager.send(monitoring, tracking);
				}

				if (tracking.isCompleted()) {
					MonitoringManager.delete(monitoring);
				} else {
					monitoring.setMonitored(date);
				}

			} catch (Exception cause) {
				exception = cause;
			}
		}

		if (exception != null) {
			throw exception;
		}

		Response<String> response = new Response<String>();
		response.setStatus(OK);

		return response;
	}
}
