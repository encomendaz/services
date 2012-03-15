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
package net.encomendaz.rest.tracking;

import static net.encomendaz.rest.Response.Status.OK;
import static net.encomendaz.rest.TrackingService.MEDIA_TYPE;
import static net.encomendaz.rest.TrackingService.SERVICE_PATH;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import net.encomendaz.rest.Response;
import net.encomendaz.rest.Tracking;
import net.encomendaz.rest.util.Serializer;

@Path(SERVICE_PATH)
@Produces(MEDIA_TYPE)
public class TrackingJsonService {

	@GET
	public String search(@QueryParam("id") String id, @QueryParam("start") Integer start, @QueryParam("end") Integer end, @QueryParam("callback") String callback) {
		Response<List<Tracking>> response = new Response<List<Tracking>>();

		List<Tracking> list = TrackingManager.search(id, start, end);
		response.setStatus(OK);
		response.setData(list);

		return Serializer.json(response, callback);
	}
}
