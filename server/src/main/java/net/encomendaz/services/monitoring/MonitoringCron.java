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

import static com.google.appengine.api.datastore.FetchOptions.Builder.withChunkSize;
import static com.google.appengine.api.taskqueue.TaskOptions.Method.GET;
import static net.encomendaz.services.Response.MEDIA_TYPE;
import static net.encomendaz.services.Response.Status.OK;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import net.encomendaz.services.Response;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.appengine.api.taskqueue.TaskOptions.Builder;

@Path("/monitoring.cron")
@Produces(MEDIA_TYPE)
public class MonitoringCron {

	@GET
	@Path("/test")
	public Response<List<Entity>> test() throws Exception {
		List<Entity> result = Collections.synchronizedList(new ArrayList<Entity>());

		Query query = new Query("Monitoring").setKeysOnly();

		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		PreparedQuery preparedQuery = datastore.prepare(query);

		for (Entity entity : preparedQuery.asIterable(withChunkSize(20000))) {
			result.add(entity);
		}

		Response<List<Entity>> response = new Response<List<Entity>>();
		response.setData(result);
		response.setStatus(OK);

		return response;
	}

	@GET
	public Response<String> execute() throws Exception {
		TaskOptions taskOptions;
		Queue queue = QueueFactory.getQueue("monitoring");

		for (String id : MonitoringManager.findIds()) {
			taskOptions = Builder.withUrl("/monitoring.task");
			taskOptions.method(GET);
			taskOptions = taskOptions.param("id", id);

			queue.add(taskOptions);
		}

		Response<String> response = new Response<String>();
		response.setStatus(OK);

		return response;
	}
}
