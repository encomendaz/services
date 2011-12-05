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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.encomendaz.rest.AlreadyExistsException;
import net.encomendaz.rest.DoesNotExistException;

public class MonitoramentoManager {

	protected static Map<String, List<Monitoramento>> emails = Collections
			.synchronizedMap(new HashMap<String, List<Monitoramento>>());

	public void cadastrar(String email, String id) throws AlreadyExistsException {
		Monitoramento monitoramento = new Monitoramento();
		monitoramento.setId(id);

		List<Monitoramento> ids;

		if (listar(email) == null) {
			ids = new ArrayList<Monitoramento>();
			emails.put(email, ids);

		} else if (existe(email, id)) {
			throw new AlreadyExistsException();
		}

		ids = emails.get(email);
		ids.add(monitoramento);
	}

	public void remover(String email, String id) throws DoesNotExistException {
		if (existe(email, id)) {
			Monitoramento monitoramento = obter(email, id);
			emails.get(email).remove(monitoramento);

		} else {
			throw new DoesNotExistException();
		}
	}

	public void remover(String email) throws DoesNotExistException {
		if (emails.containsKey(email)) {
			emails.remove(email);

		} else {
			throw new DoesNotExistException();
		}
	}

	public List<Monitoramento> listar(String email) {
		return emails.get(email);
	}

	public Monitoramento obter(String email, String id) {
		Monitoramento result = null;
		Monitoramento monitoramento = new Monitoramento(id);

		if (emails.containsKey(email) && emails.get(email).contains(monitoramento)) {
			int i = emails.get(email).indexOf(monitoramento);
			result = emails.get(email).get(i);
		}

		return result;
	}

	public boolean existe(String email, String id) {
		return obter(email, id) != null;
	}
}
