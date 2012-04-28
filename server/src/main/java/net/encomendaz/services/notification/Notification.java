package net.encomendaz.services.notification;

import java.io.Serializable;

import javax.persistence.Id;

public class Notification implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private Long id;

	private String client;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
	}
}
