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

import java.util.List;

import net.encomendaz.rest.Response;

public class TrackingJsonService implements TrackingService {

	@Override
	public Response<List<Tracking>> search(String id, Integer start, Integer end) {
		Response<List<Tracking>> response = new Response<List<Tracking>>();

		// try {
		List<Tracking> result = TrackingManager.search(id, start, end);
		response.setStatus(OK);
		response.setData(result);

		// } catch (Exception cause) {
		// response.setStatus(ERROR);
		// response.setMessage(cause.getMessage());
		// }

		return response;
	}
}
