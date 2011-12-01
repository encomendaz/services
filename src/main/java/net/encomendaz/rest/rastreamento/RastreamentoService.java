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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.alfredlibrary.utilitarios.correios.Rastreamento;
import org.alfredlibrary.utilitarios.correios.RegistroRastreamento;

@Path("/rastreamento.json")
@Produces("application/json;charset=UTF-8")
public class RastreamentoService {

	@GET
	public List<RastreamentoResponse> pesquisar(@QueryParam("id") String id, @QueryParam("inicio") Integer inicio,
			@QueryParam("fim") Integer fim, @QueryParam("ordem") String ordem) {

		if (id == null || id.isEmpty()) {
			throw new IllegalArgumentException("É necessário informar a identificação do objeto via parâmetro \"id\"");
		}

		if (ordem != null && !("asc".equals(ordem) || "desc".equals(ordem))) {
			throw new IllegalArgumentException("O parâmetro \"ordem\" só aceita os valores \"asc\" ou \"desc\"");
		}

		List<RastreamentoResponse> response = new ArrayList<RastreamentoResponse>();
		List<RegistroRastreamento> registros = Rastreamento.rastrear(id);
		Collections.reverse(registros);

		int _ini = (inicio == null || inicio < 1 ? 1 : inicio);
		int _fim = (fim == null || fim > registros.size() ? registros.size() : fim);

		for (int i = _ini; i <= _fim; i++) {
			response.add(RastreamentoResponse.parse(registros.get(i - 1)));
		}

		if ("desc".equals(ordem)) {
			Collections.reverse(response);
		}

		return response;
	}
}
