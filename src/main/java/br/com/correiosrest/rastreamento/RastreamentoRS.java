package br.com.correiosrest.rastreamento;

/*
 * Correios RESTful Web Services
 * 
 * Copyright (c) 2011, Cleverson Sacramento <http://rasea.org>.
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
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.alfredlibrary.utilitarios.correios.Rastreamento;
import org.alfredlibrary.utilitarios.correios.RegistroRastreamento;

@Path("/rastreamento")
public class RastreamentoRS {

	@GET
	@Produces("application/json;charset=UTF-8")
	@Path("/{codigo}")
	public List<OcorrenciaResponse> buscar(@PathParam("codigo") String codigo) {
		OcorrenciaResponse ocorrencia;
		List<OcorrenciaResponse> ocorrencias = new ArrayList<OcorrenciaResponse>();

		for (RegistroRastreamento reg : Rastreamento.rastrear(codigo)) {
			ocorrencia = new OcorrenciaResponse();
			ocorrencia.setSituacao(reg.getAcao());
			ocorrencia.setDescricao(reg.getDetalhe());
			ocorrencia.setLocal(reg.getLocal());
			ocorrencia.setData(reg.getDataHora());

			ocorrencias.add(ocorrencia);
		}

		return ocorrencias;
	}

	// @GET
	// @Produces("application/json;charset=UTF-8")
	// @Path("/{codigo}")
	// public RastreamentoResponse buscar(@PathParam("codigo") String codigo) {
	// RastreamentoResponse rastreamento = new RastreamentoResponse();
	//
	// OcorrenciaResponse registro;
	// List<OcorrenciaResponse> registros = new ArrayList<OcorrenciaResponse>();
	//
	// for (RegistroRastreamento reg : Rastreamento.rastrear(codigo)) {
	// registro = new OcorrenciaResponse();
	// registro.setAcao(reg.getAcao());
	// registro.setDetalhe(reg.getDetalhe());
	// registro.setLocal(reg.getLocal());
	// registros.add(registro);
	// }
	//
	// rastreamento.setRegistros(registros);
	//
	// return rastreamento;
	// }
}
