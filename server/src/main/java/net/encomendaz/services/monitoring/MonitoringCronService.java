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

import static com.google.appengine.api.taskqueue.TaskOptions.Method.GET;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.appengine.api.taskqueue.TaskOptions.Builder;

@Path("/monitoring/job/check-for-update")
public class MonitoringCronService {

	@GET
	public void execute() throws Exception {
		TaskOptions taskOptions;
		Queue queue = QueueFactory.getQueue("monitoring");

		for (Monitoring monitoring : MonitoringPersistence.findAll()) {
			taskOptions = Builder.withUrl("/monitoring/task/check-for-update");
			taskOptions = taskOptions.param("clientId", monitoring.getClientId());
			taskOptions = taskOptions.param("trackId", monitoring.getTrackId());
			taskOptions.method(GET);

			queue.add(taskOptions);
		}
	}
}
