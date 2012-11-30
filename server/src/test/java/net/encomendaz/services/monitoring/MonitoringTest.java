package net.encomendaz.services.monitoring;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.junit.Test;

public class MonitoringTest {

	@Test
	public void deviceToken() {
		String received = "12345:678";

		String deviceToken = null;
		int i = 0;
		if ((i = received.indexOf(":")) > 0) {
			deviceToken = received.substring(i + 1, received.length());
			received = received.substring(0, i);
		}

		assertEquals("12345", received);
		assertEquals("678", deviceToken);
	}

	@Test
	public void order() {
		Monitoring monitoring;

		Map<String, Monitoring> monitorings = new TreeMap<String, Monitoring>();

		monitoring = new Monitoring("b", "2");
		monitorings.put(monitoring.getClientId() + "-" + monitoring.getTrackId(), monitoring);

		monitoring = new Monitoring("c", "1");
		monitorings.put(monitoring.getClientId() + "-" + monitoring.getTrackId(), monitoring);

		monitoring = new Monitoring("b", "1");
		monitorings.put(monitoring.getClientId() + "-" + monitoring.getTrackId(), monitoring);

		monitoring = new Monitoring("a", "1");
		monitorings.put(monitoring.getClientId() + "-" + monitoring.getTrackId(), monitoring);

		monitoring = new Monitoring("c", "2");
		monitorings.put(monitoring.getClientId() + "-" + monitoring.getTrackId(), monitoring);

		List<Monitoring> expectedOrder = new ArrayList<Monitoring>();

		monitoring = new Monitoring("a", "1");
		expectedOrder.add(monitoring);

		monitoring = new Monitoring("b", "1");
		expectedOrder.add(monitoring);

		monitoring = new Monitoring("b", "2");
		expectedOrder.add(monitoring);

		monitoring = new Monitoring("c", "1");
		expectedOrder.add(monitoring);

		monitoring = new Monitoring("c", "2");
		expectedOrder.add(monitoring);

		assertArrayEquals(expectedOrder.toArray(new Monitoring[0]), monitorings.values().toArray(new Monitoring[0]));
	}

	// @Test
	// public void clonable() throws CloneNotSupportedException {
	// Monitoring monitoring = new Monitoring("b", "2");
	// Monitoring clone = (Monitoring) monitoring.clone();
	//
	// Assert.assertNotSame(clone, monitoring);
	// }
}
