package net.encomendaz.services.notification;

import static org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion.NON_NULL;

import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonPropertyOrder({ "alert", "sound", "badge" })
public class Aps {

	private String alert;

	private String sound;

	private String badge;

	public String getAlert() {
		return alert;
	}

	public void setAlert(String alert) {
		this.alert = alert;
	}

	public String getSound() {
		return sound;
	}

	@JsonSerialize(include = NON_NULL)
	public void setSound(String sound) {
		this.sound = sound;
	}

	@JsonSerialize(include = NON_NULL)
	public String getBadge() {
		return badge;
	}

	public void setBadge(String badge) {
		this.badge = badge;
	}

}
