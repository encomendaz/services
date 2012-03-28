package net.encomendaz.services;

import java.util.List;

import net.encomendaz.services.Response;
import net.encomendaz.services.ServicesClient;
import net.encomendaz.services.Tracking;
import net.encomendaz.services.TrackingService;

import org.junit.Test;

public class EncomendaZTest {

	/**
	 * Por enquanto, este teste ainda não é um teste, mas em breve será!
	 */
	@Test
	public void getTrackingService() {
		TrackingService service = ServicesClient.tracking();

		Response<List<Tracking>> response;
		response = service.search("PB882615209BR", null, null);

		System.out.println("status: " + response.toString());

		for (Tracking t : response.getData()) {
			System.out.println(t.getStatus() + " : " + t.getDate());
		}

		response = service.search("PB882615209BR", 6);

		System.out.println("status: " + response.toString());

		for (Tracking t : response.getData()) {
			System.out.println(t.getStatus() + " : " + t.getDate());
		}
	}
}
