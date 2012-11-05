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

import net.encomendaz.services.monitoring.Monitoring;
import net.encomendaz.services.monitoring.MonitoringResponse;
import net.encomendaz.services.tracking.Trace;
import net.encomendaz.services.tracking.Trace.Status;
import net.encomendaz.services.tracking.TrackingResponse;

import org.junit.Test;

public class MonitoringServiceTest {

	/**
	 * Por enquanto, este teste ainda não é um teste, mas em breve será!
	 */
	@Test
	public void load() {
		EncomendaZ.setBaseURL("http://services.encomendaz.net");
		MonitoringResponse response = EncomendaZ.monitoring.search("");

		EncomendaZ.setBaseURL("http://services.sandbox.encomendaz.net");
		String clientId = "91448300404063076307502904506675:018F14CE029B3AFA3135BDB2DA37286C77EE467AA9A72F779AB2A04B8921E448";

		int i = 1;
		MonitoringResponse response2;
		for (Monitoring m : response.getData()) {
			response2 = EncomendaZ.monitoring.register(clientId, m.getTrackId(), m.getLabel());
			System.out.println(i + " : " + m.getTrackId() + " : " + response2);
			i++;
		}
	}

	@Test
	public void clean() {
		EncomendaZ.setBaseURL("http://services.encomendaz.net");
		// EncomendaZ.setBaseURL("http://services.sandbox.encomendaz.net");
		MonitoringResponse mResponse = EncomendaZ.monitoring.search("<all>");

		Trace t;
		TrackingResponse tResponse;
		int size;
		int i = 1;

		for (Monitoring m : mResponse.getData()) {
			tResponse = EncomendaZ.tracking.search(m.getTrackId());
			size = tResponse.getData().size();

			if (size > 0) {
				t = tResponse.getData().get(size - 1);
			} else {
				t = null;
			}

			if (t != null && t.getStatus() == Status.DELIVERED) {
				System.out.println(i + ": " + m.getClientId() + " - " + m.getTrackId());
				EncomendaZ.monitoring.delete(m.getClientId(), m.getTrackId());

				i++;
			}
		}
	}

	// @Test
	public void loadX() {
		EncomendaZ.setBaseURL("http://services.sandbox.encomendaz.net");
		String clientId = "91448300404063076307502904506675:018F14CE029B3AFA3135BDB2DA37286C77EE467AA9A72F779AB2A04B8921E448";

		MonitoringResponse response = EncomendaZ.monitoring.register(clientId, "XX000000000XX", "cole");
		System.out.println(response);
	}

	@Test
	public void remove() {
		EncomendaZ.setBaseURL("http://services.sandbox.encomendaz.net");
		MonitoringResponse response = EncomendaZ.monitoring.search("<all>");

		int i = 1;
		MonitoringResponse response2;
		for (Monitoring m : response.getData()) {
			System.out.println(i + ": " + m);
			response2 = EncomendaZ.monitoring.delete(m.getClientId(), m.getTrackId());
			System.out.println(i + ": " + response2);
			i++;
		}
	}
}
