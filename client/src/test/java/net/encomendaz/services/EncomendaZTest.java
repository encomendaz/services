package net.encomendaz.services;

import net.encomendaz.services.tracking.Trace;
import net.encomendaz.services.tracking.TrackingResponse;

import org.junit.Test;

public class EncomendaZTest {

	/**
	 * Por enquanto, este teste ainda não é um teste, mas em breve será!
	 */
	@Test
	public void trackingService() {
		TrackingResponse response;

		response = EncomendaZ.tracking.search("PB882615209BR", null, null);
		for (Trace t : response.getData()) {
			System.out.println(t.getStatus() + " : " + t.getDate());
		}

		System.out.println();

		response = EncomendaZ.tracking.search("PB882615209BR", 6);
		for (Trace t : response.getData()) {
			System.out.println(t.getStatus() + " : " + t.getDate());
		}
	}
}
