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

	private static void validarParametrosPesquisar(String id) {
		if (id == null || id.isEmpty()) {
			throw new IllegalArgumentException("É necessário informar a identificação do objeto via parâmetro \"id\"");
		}
	}

	public List<Tracking> pesquisar(String id) {
		return pesquisar(id, null, null);
	}

	public static List<Tracking> pesquisar(String id, Integer begin, Integer end) {

		validarParametrosPesquisar(id);

		List<Tracking> response = new ArrayList<Tracking>();
		List<RegistroRastreamento> registros = org.alfredlibrary.utilitarios.correios.Rastreamento.rastrear(id);
		Collections.reverse(registros);

		int _ini = (begin == null || begin < 1 ? 1 : begin);
		int _fim = (end == null || end > registros.size() ? registros.size() : end);

		for (int i = _ini; i <= _fim; i++) {
			response.add(Tracking.parse(registros.get(i - 1)));
		}

		return response;
	}

	public static String hash(String id) {
		TrackingManager trackingManager = new TrackingManager();
		List<Tracking> trackings = trackingManager.pesquisar(id);
		return Serializer.json(trackings);
	}
}
