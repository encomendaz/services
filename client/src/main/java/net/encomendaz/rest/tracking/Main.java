package net.encomendaz.rest.tracking;

import java.util.List;

import net.encomendaz.rest.Response;

import org.jboss.resteasy.client.ProxyFactory;

public class Main {

	public static void main(String[] args) {
		TrackingService service = ProxyFactory.create(TrackingService.class, "http://localhost:8080/encomendaz-rest");

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
