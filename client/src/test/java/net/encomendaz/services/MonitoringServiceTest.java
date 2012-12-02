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

import static net.encomendaz.services.Response.Status.OK;
import net.encomendaz.services.monitoring.Monitoring;
import net.encomendaz.services.monitoring.MonitoringResponse;

import org.junit.Test;

public class MonitoringServiceTest {

	@Test
	public void cleanSandbox() {
		EncomendaZ.setBaseURL("http://services.sandbox.encomendaz.net/admin");
		MonitoringResponse response = EncomendaZ.monitoring.search(null, null, null);

		EncomendaZ.setBaseURL("http://services.sandbox.encomendaz.net");

		for (Monitoring m : response.getData()) {
			if (!"49875341236190825424153421103956".equalsIgnoreCase(m.getClientId())
					&& !"91448300404063076307502904506675".equalsIgnoreCase(m.getClientId())) {
				EncomendaZ.monitoring.delete(m.getClientId(), m.getTrackId());
				System.out.println(m.toString());
			}
		}
	}

	@Test
	public void cleanCompleted() {
		EncomendaZ.setBaseURL("http://services.encomendaz.net/admin");
		// EncomendaZ.setBaseURL("http://services.sandbox.encomendaz.net/admin");
		MonitoringResponse response = EncomendaZ.monitoring.search(null, true, null);

		EncomendaZ.setBaseURL("http://services.encomendaz.net");
		// EncomendaZ.setBaseURL("http://services.sandbox.encomendaz.net");

		int i = 1;
		for (Monitoring m : response.getData()) {
			if (!"49875341236190825424153421103956".equalsIgnoreCase(m.getClientId())
					&& !"91448300404063076307502904506675".equalsIgnoreCase(m.getClientId())) {
				EncomendaZ.monitoring.delete(m.getClientId(), m.getTrackId());
				System.out.println(i++ + ": " + m.getClientId() + ": " + m.getTrackId() + ": ");
			}
		}
	}

	@Test
	public void loadSandbox() {
		EncomendaZ.setBaseURL("http://services.encomendaz.net/admin");
		MonitoringResponse response = EncomendaZ.monitoring.search(null, false, null);
		System.out.println(".");

		EncomendaZ.setBaseURL("http://services.sandbox.encomendaz.net");
		String clientId = "91448300404063076307502904506675:018F14CE029B3AFA3135BDB2DA37286C77EE467AA9A72F779AB2A04B8921E448";

		int i = 1;
		int ok = 0;
		MonitoringResponse response2;
		for (Monitoring m : response.getData()) {
			// response2 = EncomendaZ.monitoring.register(clientId, m.getTrackId(), m.getLabel());
			response2 = EncomendaZ.monitoring.register(clientId, m.getTrackId(), m.getLabel());

			if (response2.getStatus() == OK) {
				ok++;
			}

			System.out.println(i++ + " : " + m.getTrackId() + " : " + response2.toString());
		}

		System.out.println("Monitoramentos OK: " + ok);
	}

	@Test
	public void sql() {
//		EncomendaZ.setBaseURL("http://services.encomendaz.net/admin");
		 EncomendaZ.setBaseURL("http://services.sandbox.encomendaz.net/admin");
		MonitoringResponse response = EncomendaZ.monitoring.search(null, null, null);

		System.out.println("drop table Monitoring;");
		System.out
		.println("create table Monitoring (clientId varchar not null, trackId varchar not null);");
//		System.out
//				.println("create table Monitoring (clientId varchar not null, trackId varchar not null, primary key (clientId, trackId));");

		for (Monitoring m : response.getData()) {
			System.out.println(String.format("insert into Monitoring (clientId, trackId) values ('%s', '%s');",
					m.getClientId(), m.getTrackId()));
		}
	}

	@Test
	public void count() {
		EncomendaZ.setBaseURL("http://services.encomendaz.net/admin");
		MonitoringResponse response1 = EncomendaZ.monitoring.search(null, false, null);

		EncomendaZ.setBaseURL("http://services.sandbox.encomendaz.net/admin");
		MonitoringResponse response2 = EncomendaZ.monitoring.search(null, false, null);

		System.out.println("Distribution count: " + response1.getData().size());
		System.out.println("Sandobox count: " + response2.getData().size());
	}
}
