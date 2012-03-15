import java.util.List;

import net.encomendaz.rest.EncomendaZ;
import net.encomendaz.rest.Response;
import net.encomendaz.rest.Tracking;
import net.encomendaz.rest.TrackingService;

import org.junit.Test;

public class EncomendaZTest {

	/**
	 * Por enquanto, este teste ainda não é um teste, mas em breve será!
	 */
	@Test
	public void getService() {
		TrackingService service = EncomendaZ.getTrackingService();

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
