package net.encomendaz.rest.tracking;

import java.util.List;

import net.encomendaz.rest.Response;

import org.jboss.resteasy.client.ProxyFactory;

public class Main {

	public static void main(String[] args) {
		TrackingService proxy = ProxyFactory.create(TrackingService.class, "http://localhost:8080/encomendaz-rest");
		// ClientResponse response = (ClientResponse) proxy.search("PB882615209BR", null, null);
		Response<List<TrackingData>> response = proxy.search("PB882615209BR", null, null);

		System.out.println(response.getStatus());

		for (TrackingData t : response.getData()) {
			System.out.println(t.getStatus() + " " + t.getDate());
		}

		// List<MyJaxbClass> list = response.getEntity(new GenericType<TrackingResponse>());

	}
}
