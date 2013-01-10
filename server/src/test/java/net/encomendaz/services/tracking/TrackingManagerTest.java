package net.encomendaz.services.tracking;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import net.encomendaz.services.EncomendaZException;

import org.alfredlibrary.utilitarios.correios.Rastreamento;
import org.junit.Test;

public class TrackingManagerTest {

	@Test
	public void x() {
		Rastreamento.rastrear("RB069060794CN");
	}
	
	@Test
	public void isCompleted() throws EncomendaZException {
		Tracking tracking = TrackingManager.search("PB261442824BR");
		assertTrue(tracking.isCompleted());
	}

	@Test
	public void isValidId() {
		assertFalse(TrackingManager.isValidId(null));
		assertFalse(TrackingManager.isValidId(""));
		assertFalse(TrackingManager.isValidId("ABC"));
		assertFalse(TrackingManager.isValidId("AA123123123BB"));
		assertFalse(TrackingManager.isValidId("AA12312312312"));
		assertFalse(TrackingManager.isValidId("12123123123BB"));
		assertFalse(TrackingManager.isValidId("1212312312300"));
		assertFalse(TrackingManager.isValidId("12123123123002"));
		assertFalse(TrackingManager.isValidId("1212312312312345678"));

		assertTrue(TrackingManager.isValidId("AA473124829AA"));
		assertTrue(TrackingManager.isValidId("RA517378152CN"));
		assertTrue(TrackingManager.isValidId("RA478236464CN"));
		assertTrue(TrackingManager.isValidId("PH092352111BR"));
		assertTrue(TrackingManager.isValidId("RA288891469CN"));
		assertTrue(TrackingManager.isValidId("SG092222385BR"));
		assertTrue(TrackingManager.isValidId("CF102557845US"));
	}
}
