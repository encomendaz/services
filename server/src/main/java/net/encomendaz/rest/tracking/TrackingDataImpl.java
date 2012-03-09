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

import org.alfredlibrary.utilitarios.correios.RegistroRastreamento;

public class TrackingDataImpl extends TrackingData {

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

	@Override
	public Date getDate() {
		return parser.getDate();
	}

	@Override
	public String getCity() {
		return parser.getCity();
	}

	@Override
	public String getState() {
		return parser.getState();
	}

	@Override
	public Status getStatus() {
		return parser.getStatus();
	}

	@Override
	public String getDescription() {
		return parser.getDescription();
	}
}
