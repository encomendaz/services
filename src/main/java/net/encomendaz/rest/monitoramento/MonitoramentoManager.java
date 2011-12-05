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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.encomendaz.rest.AlreadyExistsException;
import net.encomendaz.rest.DoesNotExistException;

public class MonitoramentoManager {

	private static Map<String, List<String>> emails = new HashMap<String, List<String>>();

	public void cadastrar(String email, String id) throws AlreadyExistsException {
		List<String> ids;

		if (!emails.containsKey(email)) {
			ids = new ArrayList<String>();
			emails.put(email, ids);

		} else if (emails.get(email).contains(id)) {
			throw new AlreadyExistsException();
		}

		ids = emails.get(email);
		ids.add(id);
	}

	public void remover(String email, String id) throws DoesNotExistException {
		if (emails.containsKey(email) && emails.get(email).contains(id)) {
			emails.get(email).remove(id);

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

	public List<String> listar(String email) {
		return emails.get(email);
	}
}
