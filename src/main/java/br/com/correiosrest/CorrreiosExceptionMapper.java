package br.com.correiosrest;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.alfredlibrary.AlfredException;

@Provider
public class CorrreiosExceptionMapper implements ExceptionMapper<AlfredException> {

	@Override
	public Response toResponse(AlfredException exception) {
		return Response.status(404).build();
	}
}
