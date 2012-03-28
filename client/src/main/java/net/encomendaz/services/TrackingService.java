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
package net.encomendaz.services;

import static net.encomendaz.services.Response.MEDIA_TYPE;
import static net.encomendaz.services.Tracking.SERVICE_PATH;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import net.encomendaz.services.Response;
import net.encomendaz.services.Tracking;

@Path(SERVICE_PATH)
@Consumes(MEDIA_TYPE)
public interface TrackingService {

	@GET
	Response<List<Tracking>> search(@QueryParam("id") String id);

	@GET
	Response<List<Tracking>> search(@QueryParam("id") String id, @QueryParam("start") Integer start);

	@GET
	Response<List<Tracking>> search(@QueryParam("id") String id, @QueryParam("start") Integer start,
			@QueryParam("end") Integer end);
}
