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

import javax.ws.rs.DELETE;
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
	public Response<String> register(@FormParam("clientId") String clientId, @FormParam("trackId") String trackId,
			@FormParam("label") String label) throws MonitoringException {
		Monitoring monitoring = MonitoringManager.load(clientId, trackId);

		Response<String> response = new Response<String>();

		// System.out.println("-------------------");
		// System.out.println(monitoring.getLabel());
		// System.out.println(label);
		// System.out.println(!(monitoring.getLabel() == null ? "" : monitoring.getLabel()).equals(label == null ? "" :
		// label));
		// System.out.println("-------------------");

		if (monitoring == null) {
			monitoring = new Monitoring();
			monitoring.setTrackId(trackId);
			monitoring.setClientId(clientId);
			monitoring.setLabel(label);
			MonitoringManager.insert(monitoring);

			response.setStatus(OK);
			response.setMessage("Created successfully with id #" + monitoring.getId());

		} else if (!(monitoring.getLabel() == null ? "" : monitoring.getLabel()).equals(label == null ? "" : label)) {
			monitoring.setLabel(label);
			MonitoringManager.update(monitoring);

			response.setStatus(OK);
			response.setMessage("Label updated");

		} else {
			throw new MonitoringException("Duplicated");
		}

		return response;
	}

	@DELETE
	public Response<String> delete(@QueryParam("clientId") String clientId, @QueryParam("trackId") String trackId)
			throws MonitoringException {
		Monitoring monitoring = MonitoringManager.load(clientId, trackId);

		if (monitoring == null) {
			throw new MonitoringException("Does not exists");

		} else {
			MonitoringManager.delete(monitoring);
		}

		Response<String> response = new Response<String>();
		response.setStatus(OK);
		response.setMessage("Registered successfully with id #" + monitoring.getId());

		return response;
	}

	@GET
	public String search(@QueryParam("callback") String callback) {
		Response<List<Monitoring>> response = new Response<List<Monitoring>>();

		List<Monitoring> monitorings = MonitoringManager.findAll();
		response.setStatus(OK);
		response.setData(monitorings);

		return Serializer.json(response, callback);
	}
}
