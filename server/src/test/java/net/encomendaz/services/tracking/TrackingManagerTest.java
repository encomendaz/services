package net.encomendaz.services.tracking;

import static org.junit.Assert.assertTrue;
import net.encomendaz.services.EncomendaZException;

import org.junit.Test;

public class TrackingManagerTest {

	@Test
	public void isCompleted() throws EncomendaZException {
		Tracking tracking = TrackingManager.search("PB261442824BR");
		assertTrue(tracking.isCompleted());
	}
}
