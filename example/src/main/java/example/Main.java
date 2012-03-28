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

import java.util.List;

import net.encomendaz.rest.Response;
import net.encomendaz.rest.Response.Status;
import net.encomendaz.rest.ServiceClient;
import net.encomendaz.rest.Tracking;

public class Main {

	public static void main(String[] args) {
		Response<List<Tracking>> result = ServiceClient.tracking().search("PB916125555BR");

		if (result.getStatus() == Status.OK) {
			int count = 1;

			for (Tracking tracking : result.getData()) {

				System.out.println("\n#" + count++);
				System.out.println("Status:      " + tracking.getStatus());
				System.out.println("Date :       " + tracking.getDate());
				System.out.println("City:        " + tracking.getCity());
				System.out.println("State:       " + tracking.getState());
				System.out.println("Description: " + tracking.getDescription());
			}

		} else {
			System.out.println("Bad news: " + result.getMessage());
		}
	}
}
