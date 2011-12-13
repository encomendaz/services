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
package net.encomendaz.rest.util;

import java.io.IOException;

import org.codehaus.jackson.map.ObjectMapper;

public class Serializer {

	public static String json(Object object, String jsonp) {
		String serialized = json(object);

		if (jsonp != null && jsonp.trim().length() > 0) {
			serialized = jsonp + "(" + serialized + ")";
		}

		return serialized;
	}

	public static String json(Object object) {
		String serialized = null;

		try {
			ObjectMapper mapper = new ObjectMapper();
			serialized = mapper.writeValueAsString(object);

		} catch (IOException cause) {
			new RuntimeException(cause);
		}

		return serialized;
	}
}
