/*
 * EncomendaZ
 * 
 * Copyright (c) 2011, EncomendaZ <http://encomendaz.net>.
 * All rights reserved.
 * 
 * EncomendaZ is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation; either version 3 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, see <http://gnu.org/licenses>
 * or write to the Free Software Foundation, Inc., 51 Franklin Street,
 * Fifth Floor, Boston, MA  02110-1301, USA.
 */
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
