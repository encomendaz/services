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

import static org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion.NON_NULL;

import java.util.Date;

import net.encomendaz.rest.DateSerializer;
import net.encomendaz.rest.EnumSerializer;

import org.alfredlibrary.utilitarios.correios.RegistroRastreamento;
import org.codehaus.jackson.map.annotate.JsonSerialize;

public class TrackingDataImpl implements TrackingData {

	private Parser parser;

	private TrackingDataImpl(Parser parser) {
		this.parser = parser;
	}

	public static TrackingDataImpl parse(RegistroRastreamento registro) {
		if (registro == null) {
			throw new IllegalArgumentException("O registro de rastreamento não pode ser nulo.");
		}

		Parser parser = new CorreiosParser(registro);
		TrackingDataImpl trackingDataImpl = new TrackingDataImpl(parser);

		return trackingDataImpl;
	}

	/*
	 * (non-Javadoc)
	 * @see net.encomendaz.rest.tracking.TrackinInfo#getDate()
	 */
	@Override
	@JsonSerialize(using = DateSerializer.class)
	public Date getDate() {
		return parser.getDate();
	}

	/*
	 * (non-Javadoc)
	 * @see net.encomendaz.rest.tracking.TrackinInfo#getCity()
	 */
	@Override
	@JsonSerialize(include = NON_NULL)
	public String getCity() {
		return parser.getCity();
	}

	/*
	 * (non-Javadoc)
	 * @see net.encomendaz.rest.tracking.TrackinInfo#getState()
	 */
	@Override
	@JsonSerialize(include = NON_NULL)
	public String getState() {
		return parser.getState();
	}

	/*
	 * (non-Javadoc)
	 * @see net.encomendaz.rest.tracking.TrackinInfo#getCountry()
	 */
	@Override
	@JsonSerialize(include = NON_NULL)
	public String getCountry() {
		return parser.getCountry();
	}

	/*
	 * (non-Javadoc)
	 * @see net.encomendaz.rest.tracking.TrackinInfo#getStatus()
	 */
	@Override
	@JsonSerialize(using = EnumSerializer.class, include = NON_NULL)
	public Status getStatus() {
		return parser.getStatus();
	}

	/*
	 * (non-Javadoc)
	 * @see net.encomendaz.rest.tracking.TrackinInfo#getDescription()
	 */
	@Override
	@JsonSerialize(include = NON_NULL)
	public String getDescription() {
		return parser.getDescription();
	}
}
