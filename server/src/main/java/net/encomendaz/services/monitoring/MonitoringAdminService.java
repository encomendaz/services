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

import static com.google.appengine.api.taskqueue.TaskOptions.Method.POST;
import static net.encomendaz.services.Constants.JSON_MEDIA_TYPE;
import static net.encomendaz.services.Response.Status.OK;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import net.encomendaz.services.Response;
import net.encomendaz.services.notification.NotificationManager;
import net.encomendaz.services.tracking.Tracking;
import net.encomendaz.services.tracking.TrackingManager;
import net.encomendaz.services.util.Booleans;
import net.encomendaz.services.util.Strings;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.appengine.api.taskqueue.TaskOptions.Builder;

@Path("/admin")
public class MonitoringAdminService {

	@GET
	@Path("/monitoring.json")
	@Produces(JSON_MEDIA_TYPE)
	public Response<List<Monitoring>> search(@QueryParam("completed") String completed,
			@QueryParam("unread") String unread) throws MonitoringException {

		List<Monitoring> list = MonitoringManager.findAll();

		if (!Strings.isEmpty(completed) || !Strings.isEmpty(unread)) {
			for (Iterator<Monitoring> iter = list.iterator(); iter.hasNext();) {
				Monitoring monitoring = iter.next();

				if (!Strings.isEmpty(completed) && monitoring.isCompleted() != Booleans.valueOf(completed)) {
					iter.remove();
				} else if (!Strings.isEmpty(unread) && monitoring.isUnread() != Booleans.valueOf(unread)) {
					iter.remove();
				}
			}
		}

		Response<List<Monitoring>> response = new Response<List<Monitoring>>();
		response.setStatus(OK);
		response.setData(list);

		return response;
	}

	@GET
	@Path("/monitoring/check-for-updates")
	public void execute() throws Exception {
		TaskOptions taskOptions;
		Queue queue = QueueFactory.getQueue("monitoring");

		for (Monitoring monitoring : MonitoringManager.findAll()) {
			if (!monitoring.isCompleted()) {
				taskOptions = Builder.withUrl("/admin/monitoring/check-for-updates");
				taskOptions = taskOptions.param("clientId", monitoring.getClientId());
				taskOptions = taskOptions.param("trackId", monitoring.getTrackId());
				taskOptions.method(POST);

				queue.add(taskOptions);
			}
		}
	}

	@POST
	@Path("/monitoring/check-for-updates")
	public void execute(@FormParam("clientId") String clientId, @FormParam("trackId") String trackId) throws Exception {
		Monitoring monitoring = MonitoringManager.load(clientId, trackId);

		if (monitoring != null) {
			Date date = new Date();

			Tracking tracking = TrackingManager.search(monitoring.getTrackId());
			String hash = tracking.getHash();

			if (monitoring.getHash() == null || !monitoring.getHash().equals(hash)) {
				monitoring.setHash(hash);
				monitoring.setUpdated(date);
				monitoring.setUnread(true);

				if (tracking.isCompleted()) {
					monitoring.setCompleted(date);
				}

				MonitoringPersistence.update(monitoring);
				NotificationManager.send(monitoring, tracking);
			}
		}

		// monitoring.setMonitored(date);
	}
}
