/*
 * EncomendaZ
 * 
 * Copyright (c) 2011, EncomendaZ <http://encomendaz.net>.
 * All rights reserved.
 * 
 * EncomendaZ is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation; either version 3 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, see <http://gnu.org/licenses>
 * or write to the Free Software Foundation, Inc., 51 Franklin Street,
 * Fifth Floor, Boston, MA  02110-1301, USA.
 */
package net.encomendaz.services;

import static net.encomendaz.services.Response.Status.ERROR;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class EncomendaZExceptionMapper implements ExceptionMapper<EncomendaZException> {

	@Override
	public Response toResponse(EncomendaZException exception) {
		net.encomendaz.services.Response<Object> response = new net.encomendaz.services.Response<Object>();
		response.setMessage(exception.getMessage());
		response.setStatus(ERROR);

		// Writer writer = new StringWriter();
		// PrintWriter printWriter = new PrintWriter(writer);
		// exception.printStackTrace(printWriter);
		// response.setMessage(writer.toString());

		return Response.ok(response).build();
	}
}
