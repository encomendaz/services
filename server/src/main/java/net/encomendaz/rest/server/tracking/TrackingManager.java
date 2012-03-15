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
package net.encomendaz.rest.server.tracking;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.encomendaz.rest.server.util.Serializer;
import net.encomendaz.rest.tracking.Tracking;

import org.alfredlibrary.utilitarios.correios.Rastreamento;
import org.alfredlibrary.utilitarios.correios.RegistroRastreamento;

public class TrackingManager {

	private static void validateParameters(String id) {
		if (id == null || id.isEmpty()) {
			throw new IllegalArgumentException("É necessário informar a identificação do objeto via parâmetro \"id\"");
		}
	}

	public List<Tracking> search(String id) {
		return search(id, null, null);
	}

	public static List<Tracking> search(String id, Integer start, Integer end) {
		validateParameters(id);

		List<Tracking> response = new ArrayList<Tracking>();
		List<RegistroRastreamento> list = Rastreamento.rastrear(id);
		Collections.reverse(list);

		int _start = (start == null || start < 1 ? 1 : start);
		int _end = (end == null || end > list.size() ? list.size() : end);

		for (int i = _start; i <= _end; i++) {
			response.add(TrackingParser.parse(list.get(i - 1)));
		}

		return response;
	}

	public static String hash(String id) {
		TrackingManager trackingManager = new TrackingManager();
		List<Tracking> trackingInfoImpls = trackingManager.search(id);

		return Serializer.json(trackingInfoImpls);
	}
}
