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
package net.encomendaz.services.tracking;

import static net.encomendaz.services.Constants.JSON_MEDIA_TYPE;
import static net.encomendaz.services.Response.Status.OK;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import net.encomendaz.services.EncomendaZException;
import net.encomendaz.services.Response;
import net.encomendaz.services.util.Serializer;

@Path("/tracking.json")
@Produces(JSON_MEDIA_TYPE)
public class TrackingService {

	@GET
	public String search(@QueryParam("id") String id, @QueryParam("start") Integer start,
			@QueryParam("end") Integer end, @QueryParam("clientId") String clientId,
			@QueryParam("callback") String callback) throws EncomendaZException {
		Response<List<Trace>> response = new Response<List<Trace>>();

		Tracking tracking = TrackingManager.search(id, start, end, clientId);

		List<Trace> traces = new ArrayList<Trace>();
		if (tracking != null) {
			traces = tracking.getTraces();
		}

		response.setStatus(OK);
		response.setData(traces);

		return Serializer.json(response, callback);
	}
}
