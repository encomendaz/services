package net.encomendaz.services;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

@Provider
@Produces("text/csv")
public class PlainTestProvider implements MessageBodyWriter<Object> {

	@Override
	public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return List.class.isAssignableFrom(type);
	}

	@Override
	public long getSize(Object t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return -1;
	}

	@Override
	public void writeTo(Object t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException,
			WebApplicationException {
		List<String[]> lines = (List<String[]>) t;
		StringBuilder csv = new StringBuilder();
		for (String[] line : lines) {
			for (String field : line) {
				csv.append(field).append(",");
			}
			csv.deleteCharAt(csv.length() - 1);
			csv.append("\r\n");
		}
		
		entityStream.write(csv.toString().getBytes());
	}
}
