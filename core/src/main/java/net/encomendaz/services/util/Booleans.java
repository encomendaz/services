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

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

import java.util.UnknownFormatConversionException;

public class Booleans {

	public static Boolean valueOf(final String string) throws UnknownFormatConversionException {
		Boolean result = null;

		if (string != null) {
			if (string.equalsIgnoreCase("yes") || string.equalsIgnoreCase(TRUE.toString())) {
				result = TRUE;
			} else if (string.equalsIgnoreCase("no") || string.equalsIgnoreCase(FALSE.toString())) {
				result = FALSE;
			}
		}

		return result;
	}
}
