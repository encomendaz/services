package net.encomendaz.services;

import net.encomendaz.services.tracking.TrackingData;
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
		for (TrackingData t : response.getData()) {
			System.out.println(t.getStatus() + " : " + t.getDate());
		}

		System.out.println();

		response = EncomendaZ.tracking.search("PB882615209BR", 6);
		for (TrackingData t : response.getData()) {
			System.out.println(t.getStatus() + " : " + t.getDate());
		}
	}
	
	@Test
	@SuppressWarnings("deprecation")
	public void deprecatedTrackingService() {
		TrackingResponse response;
		
		response = ServicesClient.tracking().search("PB882615209BR", null, null);
		for (TrackingData t : response.getData()) {
			System.out.println(t.getStatus() + " : " + t.getDate());
		}
		
		System.out.println();
		
		response = EncomendaZ.tracking.search("PB882615209BR", 6);
		for (TrackingData t : response.getData()) {
			System.out.println(t.getStatus() + " : " + t.getDate());
		}
	}
}
