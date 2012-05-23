package net.encomendaz.services.tracking;

import static org.junit.Assert.assertEquals;

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

}
