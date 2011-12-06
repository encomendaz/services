package net.encomendaz.rest.util;

import java.io.IOException;

import org.codehaus.jackson.map.ObjectMapper;

public class Serializer {

	public static String json(Object object) throws IOException {
		ObjectMapper mapper = new ObjectMapper();

		return mapper.writeValueAsString(object);
	}
}
