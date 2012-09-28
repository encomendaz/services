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

import net.encomendaz.services.tracking.Trace;
import net.encomendaz.services.tracking.TrackingResponse;

import org.junit.Test;

public class TrackingServiceTest {

	/**
	 * Por enquanto, este teste ainda não é um teste, mas em breve será!
	 */
	@Test
	// @Ignore
	public void trackingService() {
		EncomendaZ.setBaseURL("http://services.sandbox.encomendaz.net");
		TrackingResponse response;

		response = EncomendaZ.tracking.search("PB261442824BR");
		for (Trace t : response.getData()) {
			System.out.println(t.getStatus() + " : " + t.getDate() + " : ");
		}

		System.out.println();

		response = EncomendaZ.tracking.search("PB882615209BR", 6);
		for (Trace t : response.getData()) {
			System.out.println(t.getStatus() + " : " + t.getDate());
		}
	}
}
