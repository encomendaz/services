package net.encomendaz.rest.util;

import java.io.IOException;

import org.codehaus.jackson.map.ObjectMapper;

public class Serializer {

	private static Serializer instance;

	private Serializer() {
	}

	public static synchronized Serializer getInstance() {
		if (instance == null) {
			instance = new Serializer();
		}

		return instance;
	}

	public String json(Object object) throws IOException {
		ObjectMapper mapper = new ObjectMapper();

		return mapper.writeValueAsString(object);
	}
}
