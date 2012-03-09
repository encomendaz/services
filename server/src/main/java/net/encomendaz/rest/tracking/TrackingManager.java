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
package net.encomendaz.rest.tracking;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.encomendaz.rest.util.Serializer;

import org.alfredlibrary.utilitarios.correios.RegistroRastreamento;

public class TrackingManager {

	private static void validateParameters(String id) {
		if (id == null || id.isEmpty()) {
			throw new IllegalArgumentException("É necessário informar a identificação do objeto via parâmetro \"id\"");
		}
	}

	public List<TrackingData> track(String id) {
		return track(id, null, null);
	}

	public static List<TrackingData> track(String id, Integer start, Integer end) {

		validateParameters(id);

		List<TrackingData> response = new ArrayList<TrackingData>();
		List<RegistroRastreamento> infos = org.alfredlibrary.utilitarios.correios.Rastreamento.rastrear(id);
		Collections.reverse(infos);

		int _start = (start == null || start < 1 ? 1 : start);
		int _end = (end == null || end > infos.size() ? infos.size() : end);

		for (int i = _start; i <= _end; i++) {
			response.add(TrackingDataImpl.parse(infos.get(i - 1)));
		}

		return response;
	}

	public static String hash(String id) {
		TrackingManager trackingManager = new TrackingManager();
		List<TrackingData> trackingInfoImpls = trackingManager.track(id);
		return Serializer.json(trackingInfoImpls);
	}
}
