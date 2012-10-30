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

import static net.encomendaz.services.Constants.JSON_MEDIA_TYPE;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

@Path("/monitoring.json")
@Consumes(JSON_MEDIA_TYPE)
public interface MonitoringService {

	@GET
	MonitoringResponse search(@QueryParam("clientId") String clientId);

	@PUT
	MonitoringResponse register(@FormParam("clientId") String clientId, @FormParam("trackId") String trackId,
			@FormParam("label") String label);

	@DELETE
	MonitoringResponse delete(@QueryParam("clientId") String clientId, @QueryParam("trackId") String trackId);
}
