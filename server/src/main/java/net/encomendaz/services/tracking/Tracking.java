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

	public boolean isCompleted() {
		boolean result = false;

		if (traces != null && !traces.isEmpty()) {
			Trace trace = traces.get(traces.size() - 1);

			if (trace.getStatus() == Status.DELIVERED) {
				result = true;
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
