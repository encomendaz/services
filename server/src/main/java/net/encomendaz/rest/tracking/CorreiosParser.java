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
package net.encomendaz.rest.tracking;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.alfredlibrary.utilitarios.correios.RegistroRastreamento;

public class CorreiosParser implements Parser {

	private static final Pattern pattern = Pattern.compile("^(.*?[A-Z]{2,3}) (.*) - (.*)/(\\w{2})$");

	private final RegistroRastreamento registro;

	private String city;

	private String state;

	private String country;

	private String description;

	public CorreiosParser(RegistroRastreamento registro) {
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

			city = Strings.parseToFirstUpper(city);
			country = "BR";
		}
	}

	private void initDescription() {
		String text = registro.getDetalhe();
		Matcher matcher = pattern.matcher(text == null ? "" : text);

		if (matcher.matches()) {
			String p1 = matcher.group(1);
			String p2;
			String p3 = matcher.group(4);

			if (matcher.group(2).equals(matcher.group(3))) {
				p2 = matcher.group(2);
			} else {
				p2 = matcher.group(2) + " – " + matcher.group(3);
			}

			description = p1 + " " + Strings.parseToFirstUpper(p2) + "/" + p3;
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
	public String getCountry() {
		return country;
	}

	@Override
	public String getStatus() {
		return registro.getAcao();
	}

	@Override
	public String getDescription() {
		return description;
	}
}
