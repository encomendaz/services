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

import static org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion.NON_NULL;

import java.util.Date;

import net.encomendaz.services.serializer.DateDeserializer;
import net.encomendaz.services.serializer.DateSerializer;
import net.encomendaz.services.serializer.TrackingStatusDeserializer;
import net.encomendaz.services.serializer.TrackingStatusSerializer;

import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonPropertyOrder({ "date", "city", "state", "country", "status", "description" })
public class TrackingData {

	public static final String SERVICE_PATH = "/tracking.json";
	
	private Date date;

	private String city;

	private String state;

	private Status status;

	private String description;

	public enum Status {

		ACCEPTANCE, ENROUTE, CHECKED, DELIVERING, DELIVERED, UNKNOWN, AWAITING
	}

	@JsonSerialize(using = DateSerializer.class)
	public Date getDate() {
		return date;
	}

	@JsonDeserialize(using = DateDeserializer.class)
	public void setDate(Date date) {
		this.date = date;
	}

	@JsonSerialize(include = NON_NULL)
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@JsonSerialize(include = NON_NULL)
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@JsonSerialize(using = TrackingStatusSerializer.class, include = NON_NULL)
	public Status getStatus() {
		return status;
	}

	@JsonDeserialize(using = TrackingStatusDeserializer.class)
	public void setStatus(Status status) {
		this.status = status;
	}

	@JsonSerialize(include = NON_NULL)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
