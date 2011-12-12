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
package net.encomendaz.rest.monitoramento;

import java.util.List;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import net.encomendaz.rest.AlreadyExistsException;

@Path("/monitoramento.json")
@Produces("application/json;charset=UTF-8")
public class MonitoramentoService {

	@PUT
	public void cadastrar(@FormParam("id") String id, @FormParam("email") String email) throws AlreadyExistsException {
		Monitoramento monitoramento = new Monitoramento();
		monitoramento.setId(id);
		monitoramento.setEmail(email);

		MonitoramentoManager.getInstance().cadastrar(monitoramento);
	}

	@GET
	public List<Monitoramento> listar(@QueryParam("email") String email) {
		return MonitoramentoManager.getInstance().obter(email);
	}
}
