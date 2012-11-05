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

import static net.encomendaz.services.tracking.Trace.Status.AWAITING;
import static net.encomendaz.services.tracking.Trace.Status.UNKNOWN;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.encomendaz.services.util.Strings;

import org.alfredlibrary.utilitarios.correios.RegistroRastreamento;

public class CorreiosTrace extends Trace {

	private static final Pattern descriptionPattern = Pattern.compile("^(.*?[A-Z]{2,3}) (.*) - (.*)/(\\w{2})$");

	private static final Pattern localePattern = Pattern.compile("(((.+)-)|(.+)) (.+)/(\\w{2})$");

	private final RegistroRastreamento registro;

	private String city;

	private String state;

	private String description;

	private Status status;

	public CorreiosTrace(RegistroRastreamento registro) {
		this.registro = registro;

		initLocation();
		initDescription();
	}

	private void initLocation() {
		String text = registro.getLocal().replaceAll(" +", " ").trim();
		Matcher matcher = localePattern.matcher(text == null ? "" : text);

		if (matcher.matches()) {
			city = Strings.firstToUpper(matcher.group(5)).trim();
			city = applyReplcements(city);

			state = matcher.group(6).trim();
		}
	}

	private void initDescription() {
		if (!Strings.isEmpty(registro.getDetalhe())) {
			String text = registro.getDetalhe().replaceAll(" +", " ").trim();
			Matcher matcher = descriptionPattern.matcher(text);

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

			} else if (!Strings.isEmpty(registro.getAcao()) && (getStatus() == UNKNOWN || getStatus() == AWAITING)) {
				description = registro.getAcao().trim() + ". " + text + ".";
				description = description.replace("..", ".").trim();

			} else {
				description = text;
			}

		} else if (registro.getAcao().indexOf(" ") > 0) {
			description = registro.getAcao();
		}

		description = applyReplcements(description);
	}

	private String applyReplcements(final String text) {
		String result = null;

		if (text != null) {
			result = new String(text);

			result = result.replace("UNIDADE DE TRATAMENTO INTERNACIONAL - BRASIL",
					"Unidade de Tratamento Internacional - Brasil");
			result = result.replace("FISCALIZACAO ADUANEIRA", "Fiscalização Aduaneira");
			result = result.replace("TRIBUTADO-EMISSÃO NOTA TRIBUTACAO/BR", "Tributado-Emissão Nota Tributação/BR");
			result = result.replace("Sao Paulo", "São Paulo");
			result = result.replace("Brasilia", "Brasília");
		}

		return result;
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
		if (status == null) {
			final String acao = registro.getAcao().toLowerCase().trim();

			if ("postado".equals(acao)) {
				status = Status.ACCEPTANCE;

			} else if (acao.indexOf("postagem") >= 0) {
				status = Status.ACCEPTANCE;

			} else if (acao.indexOf("coletado") >= 0) {
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
