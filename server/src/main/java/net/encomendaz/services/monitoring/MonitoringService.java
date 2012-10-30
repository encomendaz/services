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
package net.encomendaz.services.monitoring;

import java.util.ArrayList;
import java.util.List;

import net.encomendaz.services.notification.NotificationManager;
import net.encomendaz.services.util.Strings;

public class MonitoringService {

	public static void register(String clientId, String trackId, String label) throws MonitoringException {
		validateClientId(clientId);
		validateTrackId(trackId);

		String deviceToken = null;
		int i;
		if ((i = clientId.indexOf(":")) > 0) {
			deviceToken = clientId.substring(i + 1, clientId.length());
			clientId = clientId.substring(0, i);

			NotificationManager.register(deviceToken, clientId);
		}

		Monitoring monitoring = MonitoringManager.load(clientId, trackId);

		if (monitoring == null) {
			monitoring = new Monitoring(clientId, trackId);
			monitoring.setLabel("".equals(label) ? null : label);
			MonitoringManager.insert(monitoring);

		} else if (!(monitoring.getLabel() == null ? "" : monitoring.getLabel()).equals(label == null ? "" : label)) {
			monitoring.setLabel(label);
			MonitoringManager.update(monitoring);

		} else {
			throw new MonitoringException("Duplicado");
		}
	}

	public static List<Monitoring> search(String clientId, String trackId) throws MonitoringException {
		validateClientId(clientId);
		List<Monitoring> result = new ArrayList<Monitoring>();

		if (clientId.equalsIgnoreCase("<all>")) {
			result = MonitoringManager.findAll();

		} else if (!Strings.isEmpty(trackId)) {
			result.add(MonitoringManager.load(clientId, trackId));

		} else {
			result = MonitoringManager.find(clientId);
		}

		return result;
	}

	public static void delete(String clientId, String trackId) throws MonitoringException {
		validateClientId(clientId);
		validateTrackId(trackId);

		Monitoring monitoring = MonitoringManager.load(clientId, trackId);

		if (monitoring == null) {
			throw new MonitoringException("Não existe monitoramento para o clientId=" + clientId + " e trackId="
					+ trackId);

		} else {
			MonitoringManager.delete(monitoring);
		}
	}

	private static void validateClientId(String clientId) throws MonitoringException {
		if (Strings.isEmpty(clientId)) {
			throw new MonitoringException("O parâmetro clientId é obrigatório");
		}
	}

	private static void validateTrackId(String trackId) throws MonitoringException {
		if (Strings.isEmpty(trackId)) {
			throw new MonitoringException("O parâmetro trackId é obrigatório");
		}
	}
}
