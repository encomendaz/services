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
import java.util.Iterator;
import java.util.List;

import net.encomendaz.services.EncomendaZException;
import net.encomendaz.services.notification.NotificationManager;
import net.encomendaz.services.util.Strings;

public class MonitoringManager {

	public static void register(String clientId, String trackId, String label) throws EncomendaZException {
		validateClientId(clientId);
		validateTrackId(trackId);

		String deviceToken = null;
		int i;
		if ((i = clientId.indexOf(":")) > 0) {
			deviceToken = clientId.substring(i + 1, clientId.length());
			clientId = clientId.substring(0, i);

			NotificationManager.register(deviceToken, clientId);
		}

		Monitoring monitoring = load(clientId, trackId);

		if (monitoring == null) {
			monitoring = new Monitoring(clientId, trackId);
			monitoring.setLabel("".equals(label) ? null : label);
			monitoring.setUnread(false);

			MonitoringPersistence.insert(monitoring);

		} else if (!(monitoring.getLabel() == null ? "" : monitoring.getLabel()).equals(label == null ? "" : label)) {
			monitoring.setLabel(label);
			MonitoringPersistence.update(monitoring);

		} else {
			throw new MonitoringException("Duplicado");
		}
	}

	public static void markAsRead(String clientId, String trackId) throws MonitoringException {
		Monitoring monitoring = load(clientId, trackId);

		if (monitoring != null) {
			monitoring.setUnread(false);
			MonitoringPersistence.update(monitoring);
		}
	}

	public static Monitoring load(String clientId, String trackId) throws MonitoringException {
		validateClientId(clientId);
		validateTrackId(trackId);

		return MonitoringPersistence.load(clientId, trackId);
	}

	public static List<Monitoring> findAll() {
		return MonitoringPersistence.findAll();
	}

	public static Integer countUnread(String clientId) {
		int count = 0;

		if (!Strings.isEmpty(clientId)) {
			List<Monitoring> list;

			try {
				list = search(clientId, null, true);
			} catch (MonitoringException cause) {
				list = new ArrayList<Monitoring>();
			}

			count = list.size();
		}

		return count;
	}

	public static List<Monitoring> search(String clientId, String trackId, Boolean unread) throws MonitoringException {
		validateClientId(clientId);
		List<Monitoring> result;

		if (Strings.isEmpty(trackId)) {
			result = MonitoringPersistence.find(clientId);

		} else {
			result = new ArrayList<Monitoring>();
			result.add(load(clientId, trackId));
		}

		if (unread != null) {
			Monitoring monitoring;

			for (Iterator<Monitoring> iter = result.iterator(); iter.hasNext();) {
				monitoring = iter.next();

				if (!unread.equals(monitoring.isUnread())) {
					iter.remove();
				}
			}
		}

		return result;
	}

	public static void delete(String clientId, String trackId) throws MonitoringException {
		validateClientId(clientId);
		validateTrackId(trackId);

		Monitoring monitoring = MonitoringPersistence.load(clientId, trackId);

		if (monitoring == null) {
			throw new MonitoringException("Não existe monitoramento para o clientId=" + clientId + " e trackId="
					+ trackId);

		} else {
			MonitoringPersistence.delete(monitoring);
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
