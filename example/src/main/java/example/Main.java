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
package example;

import net.encomendaz.services.EncomendaZ;
import net.encomendaz.services.Response.Status;
import net.encomendaz.services.tracking.TrackingData;
import net.encomendaz.services.tracking.TrackingResponse;

public class Main {

	public static void main(String[] args) {
		TrackingResponse response;

		response = EncomendaZ.tracking.search("PB916125555BR");

		if (response.getStatus() == Status.OK) {
			int count = 1;

			for (TrackingData trackingData : response.getData()) {

				System.out.println("\n#" + count++);
				System.out.println("Status:      " + trackingData.getStatus());
				System.out.println("Date :       " + trackingData.getDate());
				System.out.println("City:        " + trackingData.getCity());
				System.out.println("State:       " + trackingData.getState());
				System.out.println("Description: " + trackingData.getDescription());
			}

		} else {
			System.out.println("Bad news: " + response.getMessage());
		}
	}
}
