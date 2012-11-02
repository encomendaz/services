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
package net.encomendaz.services.util;

public class Strings {

	public static boolean isEmpty(final String string) {
		return string == null || string.trim().length() == 0;
	}
	
	public static String firstToUpper(final String string) {
		StringBuffer buffer = new StringBuffer();

		for (String part : string.split(" ")) {
			if (part.length() > 2) {
				buffer.append(part.substring(0, 1).toUpperCase() + part.substring(1).toLowerCase());
			} else {
				buffer.append(part.toLowerCase());
			}

			buffer.append(" ");
		}

		return buffer.toString().trim();
	}
}
