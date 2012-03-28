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
package net.encomendaz.services.monitoramento;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.encomendaz.services.tracking.TrackingManager;

public class MonitoramentoManager {

	protected static Map<String, List<Monitoramento>> emails = Collections
			.synchronizedMap(new HashMap<String, List<Monitoramento>>());

	public static void cadastrar(Monitoramento monitoramento) throws AlreadyExistsException {
		List<Monitoramento> ids;

		if (obter(monitoramento.getEmail()) == null) {
			ids = new ArrayList<Monitoramento>();
			emails.put(monitoramento.getEmail(), ids);

		} else if (existe(monitoramento)) {
			throw new AlreadyExistsException();
		}

		ids = emails.get(monitoramento.getEmail());
		ids.add(monitoramento);

		String hash = TrackingManager.hash(monitoramento.getId());
		monitoramento.setHash(hash);
		monitoramento.setUpdated(new Date());
	}

	public static void remover(Monitoramento monitoramento) throws DoesNotExistException {
		if (existe(monitoramento)) {
			emails.get(monitoramento.getEmail()).remove(monitoramento);

		} else {
			throw new DoesNotExistException();
		}
	}

	public static void remover(String email) throws DoesNotExistException {
		if (emails.containsKey(email)) {
			emails.remove(email);

		} else {
			throw new DoesNotExistException();
		}
	}

	public static List<String> obter() {
		return new ArrayList<String>(emails.keySet());
	}

	public static List<Monitoramento> obter(String email) {
		return emails.get(email);
	}

	public static Monitoramento obter(Monitoramento monitoramento) {
		Monitoramento result = null;

		if (emails.containsKey(monitoramento.getEmail())
				&& emails.get(monitoramento.getEmail()).contains(monitoramento)) {
			int i = emails.get(monitoramento.getEmail()).indexOf(monitoramento);
			result = emails.get(monitoramento.getEmail()).get(i);
		}

		return result;
	}

	public static boolean existe(Monitoramento monitoramento) {
		return obter(monitoramento) != null;
	}
}
