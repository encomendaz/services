package net.encomendaz.rest.tracking;

public enum Status {

	DELIVERED, ENROUTE, ACCEPTANCE;

	public String toString() {
		return name().toLowerCase();
	}
}
