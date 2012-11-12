package net.encomendaz.services.tracking;

import java.util.List;

import net.encomendaz.services.tracking.Trace.Status;
import net.encomendaz.services.util.Hasher;
import net.encomendaz.services.util.Serializer;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

@JsonPropertyOrder({ "id", "traces" })
public class Tracking {

	private String id;

	private List<Trace> traces;

	private String hash;

	public String getHash() {
		if (this.hash == null) {
			String serial = Serializer.json(getTraces());
			this.hash = Hasher.sha1(serial);
		}

		return this.hash;
	}

	@JsonIgnore
	public boolean isCompleted() {
		boolean result = false;
		List<Trace> traces = getTraces();

		if (traces != null) {
			for (Trace trace : traces) {
				if (trace.getStatus() == Status.DELIVERED) {
					result = true;
					break;
				}
			}
		}

		return result;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<Trace> getTraces() {
		return traces;
	}

	public void setTraces(List<Trace> traces) {
		this.traces = traces;
	}

	@JsonIgnore
	public Trace getLastTrace() {
		return this.traces.isEmpty() ? null : this.traces.get(this.traces.size() - 1);
	}
}
