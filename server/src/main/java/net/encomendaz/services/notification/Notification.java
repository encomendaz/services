package net.encomendaz.services.notification;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonPropertyOrder;

@JsonPropertyOrder({ "aliases", "aps" })
public class Notification {

	private Aps aps;

	private List<String> aliases = new ArrayList<String>();

	public Aps getAps() {
		return aps;
	}

	public void setAps(Aps aps) {
		this.aps = aps;
	}

	public List<String> getAliases() {
		return aliases;
	}

	public void setAliases(List<String> aliases) {
		this.aliases = aliases;
	}

	public void addAlias(String alias) {
		this.aliases.add(alias);
	}
}
