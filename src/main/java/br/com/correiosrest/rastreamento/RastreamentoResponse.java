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

import java.util.List;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(FIELD)
@XmlRootElement(name = "rastreamento")
public class RastreamentoResponse {

	@XmlElement(name = "registro")
	@XmlElementWrapper(name = "registros")
	private List<RegistroResponse> registros;

	public List<RegistroResponse> getRegistros() {
		return registros;
	}

	public void setRegistros(List<RegistroResponse> registros) {
		this.registros = registros;
	}

}
