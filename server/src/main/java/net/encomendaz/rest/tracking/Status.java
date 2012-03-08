package net.encomendaz.rest.tracking;

public enum Status {

	DELIVERED, ENROUTE;

	public String toString() {
		return name().toLowerCase();
	}
}
