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

import static net.encomendaz.services.tracking.TrackingData.Status.UNKNOWN;
import static net.encomendaz.services.tracking.TrackingData.Status.AWAITING;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.encomendaz.services.util.Strings;

import org.alfredlibrary.utilitarios.correios.RegistroRastreamento;

public class CorreiosTrackingData extends TrackingData {

	private static final Pattern pattern = Pattern.compile("^(.*?[A-Z]{2,3}) (.*) - (.*)/(\\w{2})$");

	private final RegistroRastreamento registro;

	private String city;

	private String state;

	private String description;

	private Status status;

	public CorreiosTrackingData(RegistroRastreamento registro) {
		this.registro = registro;

		initLocation();
		initDescription();
	}

	private void initLocation() {
		String text = registro.getLocal();
		Matcher matcher = pattern.matcher(text == null ? "" : text);

		if (matcher.matches()) {
			state = matcher.group(4);

			if (matcher.group(2).equals(matcher.group(3))) {
				city = matcher.group(2);
			} else {
				city = matcher.group(2) + " – " + matcher.group(3);
			}

			city = Strings.firstToUpper(city);
		}
	}

	private void initDescription() {
		if (!Strings.isEmpty(registro.getDetalhe())) {
			Matcher matcher = pattern.matcher(registro.getDetalhe());

			if (matcher.matches()) {
				String p1 = matcher.group(1);
				String p2;
				String p3 = matcher.group(4);

				if (matcher.group(2).equals(matcher.group(3))) {
					p2 = matcher.group(2);
				} else {
					p2 = matcher.group(2) + " – " + matcher.group(3);
				}

				description = p1 + " " + Strings.firstToUpper(p2) + "/" + p3;

			} else {
				description = "";
				
				if (!Strings.isEmpty(registro.getAcao()) && (getStatus() == UNKNOWN || getStatus() == AWAITING )) {
					description += registro.getAcao().trim() + ". ";
				}

				description += registro.getDetalhe().trim() + ".";
				description = description.replaceAll("[.][.]", ".").trim();
			}

		} else if (registro.getAcao().indexOf(" ") > 0) {
			description = registro.getAcao();
		}
	}

	@Override
	public Date getDate() {
		return registro.getDataHora();
	}

	@Override
	public String getCity() {
		return city;
	}

	@Override
	public String getState() {
		return state;
	}

	@Override
	public Status getStatus() {
		final String acao = registro.getAcao().toLowerCase().trim();

		if (status == null) {
			if ("postado".equals(acao)) {
				status = Status.ACCEPTANCE;
				
			} else if (acao.indexOf("postagem") >= 0) {
					status = Status.ACCEPTANCE;

			} else if ("encaminhado".equals(acao)) {
				status = Status.ENROUTE;

			} else if ("conferido".equals(acao)) {
				status = Status.CHECKED;

			} else if ("saiu para entrega".equals(acao)) {
				status = Status.DELIVERING;

			} else if ("entregue".equals(acao)) {
				status = Status.DELIVERED;

			} else if ("entrega efetuada".equals(acao)) {
				status = Status.DELIVERED;

			} else if (acao.indexOf("aguardando") >= 0) {
				status = Status.AWAITING;
				
			} else {
				status = Status.UNKNOWN;
			}
		}

		return status;
	}

	@Override
	public String getDescription() {
		return description;
	}
}
