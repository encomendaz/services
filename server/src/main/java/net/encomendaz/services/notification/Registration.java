package net.encomendaz.services.notification;

import org.codehaus.jackson.annotate.JsonPropertyOrder;

@JsonPropertyOrder({ "alias" })
public class Registration {

	private String alias;

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}
}
