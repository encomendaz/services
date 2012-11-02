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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import net.encomendaz.services.util.Booleans;

import org.junit.Test;

public class BooleansTest {

	@Test
	public void valueOf() {
		assertTrue(Booleans.valueOf("True"));
		assertTrue(Booleans.valueOf("true"));
		assertTrue(Booleans.valueOf("TRUE"));
		assertTrue(Booleans.valueOf("Yes"));
		assertTrue(Booleans.valueOf("yes"));
		assertTrue(Booleans.valueOf("YES"));

		assertFalse(Booleans.valueOf("False"));
		assertFalse(Booleans.valueOf("false"));
		assertFalse(Booleans.valueOf("FALSE"));
		assertFalse(Booleans.valueOf("No"));
		assertFalse(Booleans.valueOf("no"));
		assertFalse(Booleans.valueOf("NO"));

		assertNull(Booleans.valueOf(null));
		assertNull(Booleans.valueOf(""));
		assertNull(Booleans.valueOf("x"));
	}
}
