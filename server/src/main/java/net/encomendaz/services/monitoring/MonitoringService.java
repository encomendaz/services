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

import java.util.List;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import net.encomendaz.services.Response;
import net.encomendaz.services.util.Serializer;

@Path("/monitoring.json")
@Produces(MEDIA_TYPE)
public class MonitoringService {

	@PUT
	public Response<String> insert(@FormParam("trackId") String trackId, @FormParam("clientId") String clientId) {
		Monitoring monitoring = new Monitoring();
		monitoring.setClientId(clientId);
		monitoring.setTrackId(trackId);

		MonitoringManager.insert(monitoring);

		Response<String> response = new Response<String>();
		response.setStatus(OK);
		response.setMessage("Monitoring was created successfully with id #" + monitoring.getId());

		return response;
	}

	@GET
	public String search(@QueryParam("callback") String callback) {
		Response<List<Monitoring>> response = new Response<List<Monitoring>>();

		List<Monitoring> monitorings = MonitoringManager.x();
		response.setStatus(OK);
		response.setData(monitorings);

		return Serializer.json(response, callback);
	}
}
