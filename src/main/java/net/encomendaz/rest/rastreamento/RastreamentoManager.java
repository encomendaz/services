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
package net.encomendaz.rest.rastreamento;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.encomendaz.rest.util.Hasher;
import net.encomendaz.rest.util.Serializer;

import org.alfredlibrary.utilitarios.correios.RegistroRastreamento;

public class RastreamentoManager {

	private void validarParametrosPesquisar(String id, String ordem) {
		if (id == null || id.isEmpty()) {
			throw new IllegalArgumentException("É necessário informar a identificação do objeto via parâmetro \"id\"");
		}

		if (ordem != null && !("asc".equals(ordem) || "desc".equals(ordem))) {
			throw new IllegalArgumentException("O parâmetro \"ordem\" só aceita os valores \"asc\" ou \"desc\"");
		}
	}

	public List<Rastreamento> pesquisar(String id) {
		return this.pesquisar(id, null, null, null);
	}

	public List<Rastreamento> pesquisar(String id, Integer inicio, Integer fim, String ordem) {

		validarParametrosPesquisar(id, ordem);

		List<Rastreamento> response = new ArrayList<Rastreamento>();
		List<RegistroRastreamento> registros = org.alfredlibrary.utilitarios.correios.Rastreamento.rastrear(id);
		Collections.reverse(registros);

		int _ini = (inicio == null || inicio < 1 ? 1 : inicio);
		int _fim = (fim == null || fim > registros.size() ? registros.size() : fim);

		for (int i = _ini; i <= _fim; i++) {
			response.add(Rastreamento.parse(registros.get(i - 1)));
		}

		if ("desc".equals(ordem)) {
			Collections.reverse(response);
		}

		return response;
	}

	public String hash(String id) {
		String json;

		try {
			RastreamentoManager rastreamentoManager = new RastreamentoManager();
			List<Rastreamento> rastreamentos = rastreamentoManager.pesquisar(id);

			json = Serializer.getInstance().json(rastreamentos);
		} catch (IOException e) {
			json = null;
		}

		return Hasher.getInstance().md5(json);
	}
}
