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

import java.util.List;

public class TrackingJsonService implements TrackingService {

	@Override
	public TrackingResponse track(String id, Integer start, Integer end) {
		TrackingResponse response = new TrackingResponseImpl();

		try {
			List<TrackingData> result = TrackingManager.track(id, start, end);
			response.setStatus("ok");
			response.setData(result);

		} catch (Exception cause) {
			response.setStatus("error");
			response.setMessage(cause.getMessage());
		}

		return response;
	}
}
