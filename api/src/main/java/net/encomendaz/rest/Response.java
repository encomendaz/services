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
package net.encomendaz.rest;

import static org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion.NON_NULL;
import net.encomendaz.rest.internal.ResponseStatusDeserializer;
import net.encomendaz.rest.internal.ResponseStatusSerializer;

import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonPropertyOrder({ "status", "message", "data" })
public class Response<D> {

	private Status status;

	private String message;

	private D data;

	public enum Status {

		OK, ERROR;
	}

	@JsonSerialize(using = ResponseStatusSerializer.class)
	public Status getStatus() {
		return status;
	}

	@JsonDeserialize(using = ResponseStatusDeserializer.class)
	public void setStatus(Status status) {
		this.status = status;
	}

	@JsonSerialize(include = NON_NULL)
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@JsonSerialize(include = NON_NULL)
	public D getData() {
		return data;
	}

	public void setData(D data) {
		this.data = data;
	}
}
