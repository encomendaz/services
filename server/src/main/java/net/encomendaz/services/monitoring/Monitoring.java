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
package net.encomendaz.services.monitoring;

import static org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion.NON_NULL;

import java.util.Date;

import javax.persistence.Id;
import javax.persistence.Transient;

import net.encomendaz.services.serializer.DateSerializer;
import net.encomendaz.services.util.Serializer;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonPropertyOrder({ "clientId", "trackId", "label", "created", "monitored", "updated", "hash" })
public class Monitoring implements Comparable<Monitoring>, Cloneable {

	public static final String SERVICE_PATH = "/monitoring.json";

	@Id
	private Long id;

	private String clientId;

	private String trackId;

	private String label;

	private Date created;

	@Transient
	private Date monitored;

	private Date updated;

	private String hash;

	public Monitoring() {
	}

	public Monitoring(String clientId, String trackId) {
		this.setClientId(clientId);
		this.setTrackId(trackId);
	}

	@Override
	protected Object clone() {
		Object clone = null;

		try {
			clone = super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}

		return clone;
	}

	@JsonIgnore
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@JsonSerialize(include = NON_NULL)
	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getTrackId() {
		return trackId;
	}

	public void setTrackId(String trackId) {
		this.trackId = trackId != null ? trackId.toUpperCase() : null;
	}

	@Override
	public int compareTo(Monitoring other) {
		int result = 0;

		if (this.getClientId() != null) {
			result = this.getClientId().compareTo(other.getClientId());
		}

		if (result == 0 && this.getTrackId() != null) {
			result = this.getTrackId().compareTo(other.getTrackId());
		}

		return result;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((clientId == null) ? 0 : clientId.hashCode());
		result = prime * result + ((trackId == null) ? 0 : trackId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Monitoring)) {
			return false;
		}
		Monitoring other = (Monitoring) obj;
		if (clientId == null) {
			if (other.clientId != null) {
				return false;
			}
		} else if (!clientId.equals(other.clientId)) {
			return false;
		}
		if (trackId == null) {
			if (other.trackId != null) {
				return false;
			}
		} else if (!trackId.equals(other.trackId)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return Serializer.json(this);
	}

	@JsonIgnore
	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	@JsonSerialize(using = DateSerializer.class, include = NON_NULL)
	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	@JsonSerialize(using = DateSerializer.class, include = NON_NULL)
	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	@JsonSerialize(using = DateSerializer.class, include = NON_NULL)
	public Date getMonitored() {
		return monitored;
	}

	public void setMonitored(Date monitored) {
		this.monitored = monitored;
	}

	@JsonSerialize(include = NON_NULL)
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
}
