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
package net.encomendaz.services.tracking;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.encomendaz.services.EncomendaZException;
import net.encomendaz.services.monitoring.MonitoringManager;
import net.encomendaz.services.util.Strings;

import org.alfredlibrary.AlfredException;
import org.alfredlibrary.utilitarios.correios.Rastreamento;
import org.alfredlibrary.utilitarios.correios.RegistroRastreamento;

public class TrackingManager {

	private static void validateParameters(String id) throws TrackingException {
		if (Strings.isEmpty(id)) {
			throw new TrackingException("É necessário informar código de rastreamento via parâmetro \"id\"");

		} else if (!isValidId(id)) {
			throw new TrackingException("O \"id\" informado não representa um código de rastreamento válido");
		}
	}

	public static boolean isValidId(String id) {
		boolean result = false;

		if (id != null) {
			Pattern idPattern = Pattern.compile("^\\w{2}(\\d{9})\\w{2}$");
			Matcher matcher = idPattern.matcher(id);

			if (matcher.matches()) {
				String number = matcher.group(1);
				int sum = 0;
				int mult[] = { 8, 6, 4, 2, 3, 5, 9, 7 };

				for (int i = 0; i < 8; i++) {
					sum += Integer.parseInt(number.substring(i, i + 1)) * mult[i];
				}

				int rest = (sum % 11);
				int digit;

				if (rest == 0) {
					digit = 5;
				} else if (rest == 1) {
					digit = 0;
				} else {
					digit = 11 - rest;
				}

				if (Integer.parseInt(number.substring(8, 9)) == digit) {
					result = true;
				}
			}
		}

		return result;
	}

	public static Tracking search(String id) throws EncomendaZException {
		return search(id, null, null, null);
	}

	public static Tracking search(String trackId, Integer start, Integer end, String clientId) throws EncomendaZException {
		validateParameters(trackId);

		List<Trace> traces = new ArrayList<Trace>();

		try {
			List<RegistroRastreamento> list = Rastreamento.rastrear(trackId);
			Collections.reverse(list);

			int _start = (start == null || start < 1 ? 1 : start);
			int _end = (end == null || end > list.size() ? list.size() : end);

			for (int i = _start; i <= _end; i++) {
				traces.add(parse(list.get(i - 1)));
			}

		} catch (AlfredException cause) {
			cause.printStackTrace();
		}

		Tracking tracking = new Tracking();
		tracking.setId(trackId);
		tracking.setTraces(traces);

		if (!Strings.isEmpty(clientId)) {
			MonitoringManager.markAsRead(clientId, trackId);
		}

		return tracking;
	}

	private static Trace parse(RegistroRastreamento registro) {
		if (registro == null) {
			throw new IllegalArgumentException("O registro de rastreamento não pode ser nulo.");
		}

		return new CorreiosTrace(registro);
	}
}
