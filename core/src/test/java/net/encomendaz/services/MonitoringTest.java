package net.encomendaz.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import net.encomendaz.services.monitoring.Monitoring;

import org.junit.Test;

public class MonitoringTest {

	@Test
	public void createId() {
		String id1;
		String id2;

		id1 = Monitoring.generateId("a", "b");
		id2 = Monitoring.generateId("a", "b");
		System.out.println(id1 + ", " + id2);
		assertEquals(id1, id2);

		id1 = Monitoring.generateId("a", "b1");
		id2 = Monitoring.generateId("a", "b2");
		System.out.println(id1 + ", " + id2);
		assertFalse(id1.equals(id2));
		
		id1 = Monitoring.generateId("ab", "c");
		id2 = Monitoring.generateId("a", "bc");
		System.out.println(id1 + ", " + id2);
		assertFalse(id1.equals(id2));
		
		id1 = Monitoring.generateId("5411077972031436343686122259383635991608236908273808014871592295", "RC167332563HK");
		id2 = Monitoring.generateId("5452121780018984264084241564177751666228184734532723216967096344", "LC496719755US");
		System.out.println(id1 + ", " + id2);
		assertFalse(id1.equals(id2));
		
		id1 = Monitoring.generateId("5411077972031436343686122259383635991608236908273808014871592295", "RC167332563HK");
		id2 = Monitoring.generateId("5411077972031436343686122259383635991608236908273808014871592295", "LC496719755US");
		System.out.println(id1 + ", " + id2);
		assertFalse(id1.equals(id2));
		
		id1 = Monitoring.generateId("5411077972031436343686122259383635991608236908273808014871592295", "LC496719755US");
		id2 = Monitoring.generateId("5452121780018984264084241564177751666228184734532723216967096344", "LC496719755US");
		System.out.println(id1 + ", " + id2);
		assertFalse(id1.equals(id2));
		
	}
}
