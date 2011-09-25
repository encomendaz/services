/*
 * Correios RESTful Web Services
 * 
 * Copyright (c) 2011, Cleverson Sacramento <http://cleversonsacramento.com>.
 * All rights reserved.
 *
 * Correios RESTful Web Services is free software; you can redistribute
 * it and/or modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License.
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
package br.com.correiosrest.rastreamento;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.alfredlibrary.utilitarios.correios.Rastreamento;
import org.alfredlibrary.utilitarios.correios.RegistroRastreamento;

@Path("/rastreamento")
public class RastreamentoRS {

	@GET
	@Produces("application/json;charset=UTF-8")
	@Path("/{codigo}")
	public List<RegistroResponse> pesquisar(@PathParam("codigo") String codigo, @QueryParam("ini") Integer inicio,
			@QueryParam("fim") Integer fim) {
		List<RegistroResponse> ocorrencias = new ArrayList<RegistroResponse>();

		List<RegistroRastreamento> registros = Rastreamento.rastrear(codigo);
		int size = registros.size();
		for (int i = 1; i <= size; i++) {
			if (i >= (inicio == null ? 1 : inicio) && i <= (fim == null ? size : fim)) {
				ocorrencias.add(RegistroResponse.parse(registros.get(i - 1)));
			}
		}

		return ocorrencias;
	}
}
