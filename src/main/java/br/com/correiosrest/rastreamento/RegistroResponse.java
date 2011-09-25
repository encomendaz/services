/*
 * Correios RESTful Web Services
 * 
 * Copyright (c) 2011, Cleverson Sacramento <http://cleversonsacramento.com>.
 * All rights reserved.
 *
 * Correios RESTful Web Services is free software; you can redistribute
 * it and/or modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License.
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
package br.com.correiosrest.rastreamento;

import static javax.xml.bind.annotation.XmlAccessType.FIELD;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.alfredlibrary.utilitarios.correios.RegistroRastreamento;

@XmlAccessorType(FIELD)
@XmlRootElement(name = "reg")
@XmlType(propOrder = { "data", "local", "situacao", "observacao" })
public class RegistroResponse {

	@XmlElement(name = "dat", required = true)
	private Date data;

	@XmlElement(name = "loc", required = true)
	private String local;

	@XmlElement(name = "sit", required = true)
	private String situacao;

	@XmlElement(name = "obs", required = false)
	private String observacao;

	private RegistroResponse() {
	}

	public static RegistroResponse parse(RegistroRastreamento registro) {
		if (registro == null) {
			throw new IllegalArgumentException("O registro de rastreamento não pode ser nulo.");
		}

		RegistroResponse response = new RegistroResponse();
		response.data = registro.getDataHora();
		response.local = parse(registro.getLocal());
		response.situacao = registro.getAcao();
		response.observacao = parse(registro.getDetalhe());

		return response;
	}

	private static String parse(String local) {
		Pattern pattern = Pattern.compile("^(.*) (.*) - \\2/(\\w{2})$");
		Matcher matcher = pattern.matcher(local == null ? "" : local);
		String resultado = local;

		if (matcher.matches()) {
			resultado = matcher.group(1) + " " + parseToFirstUpper(matcher.group(2)) + "/" + matcher.group(3);
		}

		return resultado;
	}

	private static String parseToFirstUpper(String texto) {
		StringBuffer buffer = new StringBuffer();

		for (String palavra : texto.split(" ")) {
			if (palavra.length() > 2) {
				buffer.append(palavra.substring(0, 1).toUpperCase() + palavra.substring(1).toLowerCase());
			} else {
				buffer.append(palavra.toLowerCase());
			}

			buffer.append(" ");
		}

		return buffer.toString().trim();
	}

	public String getSituacao() {
		return situacao;
	}

	public String getObservacao() {
		return observacao;
	}

	public String getLocal() {
		return local;
	}

	public Date getData() {
		return data;
	}

}
