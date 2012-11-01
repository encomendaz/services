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
package net.encomendaz.services.notification;

import static org.jboss.resteasy.util.HttpHeaderNames.CONTENT_TYPE;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;
import java.util.Map.Entry;
import java.util.ResourceBundle;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriBuilder;

import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.client.core.BaseClientResponse;
import org.jboss.resteasy.client.core.BaseClientResponse.BaseClientResponseStreamFactory;
import org.jboss.resteasy.client.core.executors.URLConnectionClientExecutor;
import org.jboss.resteasy.util.Base64;
import org.jboss.resteasy.util.CaseInsensitiveMap;
import org.jboss.resteasy.util.CommitHeaderOutputStream;

public class AirshipClientExecutor extends URLConnectionClientExecutor {

	// @Override
	// protected void setupRequest(ClientRequest request, HttpURLConnection connection) throws ProtocolException {
	//
	// ResourceBundle bundle = ResourceBundle.getBundle("encomendaz-server");
	// String username = bundle.getString("airship.username");;
	// String password = bundle.getString("airship.password");
	//
	// String authString = username + ":" + password;
	// String authStringBase64 = Base64.encodeBytes(authString.getBytes());
	// authStringBase64 = authStringBase64.trim();
	//
	// connection.addRequestProperty("Authorization", "Basic " + authStringBase64);
	//
	// super.setupRequest(request, connection);
	// }

	public ClientResponse<?> execute(ClientRequest request) throws Exception {
		String uri = request.getUri();
		String httpMethod = request.getHttpMethod();

		HttpURLConnection connection = (HttpURLConnection) new URL(uri).openConnection();
		connection.setRequestMethod(httpMethod);

		ResourceBundle bundle = ResourceBundle.getBundle("encomendaz-server");
		String appKey = bundle.getString("airship.username");
		String appMasterSecret = bundle.getString("airship.password");

		String authString = appKey + ":" + appMasterSecret;
		String authStringBase64 = Base64.encodeBytes(authString.getBytes());
		authStringBase64 = authStringBase64.trim();

		connection.addRequestProperty("Authorization", "Basic " + authStringBase64);

		setupRequest(request, connection);
		return execute(request, connection);
	}

	protected void setupRequest(ClientRequest request, HttpURLConnection connection) throws ProtocolException {
		boolean isGet = "GET".equals(request.getHttpMethod());
		connection.setInstanceFollowRedirects(isGet && request.followRedirects());
		connection.setDoOutput(request.getBody() != null || !request.getFormParameters().isEmpty());

		if (request.getBody() != null && !request.getFormParameters().isEmpty())
			throw new RuntimeException("You cannot send both form parameters and an entity body");

		if (!request.getFormParameters().isEmpty()) {
			throw new RuntimeException("URLConnectionClientExecutor doesn't support form parameters yet");
		}
	}

	private void commitHeaders(ClientRequest request, HttpURLConnection connection) {
		for (Entry<String, List<String>> entry : request.getHeaders().entrySet()) {
			String value = null;
			if (entry.getValue().size() == 1)
				value = entry.getValue().get(0);
			else {
				StringBuilder b = new StringBuilder();
				String add = "";
				for (String v : entry.getValue()) {
					b.append(add).append(v);
					add = ",";
				}
				value = b.toString();
			}
			// connection.addRequestProperty(entry.getKey(), value);
		}
	}

	public ClientRequest createRequest(String uriTemplate) {
		return new ClientRequest(uriTemplate, this);
	}

	public ClientRequest createRequest(UriBuilder uriBuilder) {
		return new ClientRequest(uriBuilder, this);
	}

	private ClientResponse execute(ClientRequest request, final HttpURLConnection connection) throws IOException {
		outputBody(request, connection);
		final int status = connection.getResponseCode();
		BaseClientResponse response = new BaseClientResponse(new BaseClientResponseStreamFactory() {

			public InputStream getInputStream() throws IOException {
				return (status < 300) ? connection.getInputStream() : connection.getErrorStream();
			}

			public void performReleaseConnection() {
				try {
					getInputStream().close();
				} catch (IOException e) {
				}
				connection.disconnect();
			}
		}, this);
		response.setProviderFactory(request.getProviderFactory());
		response.setStatus(status);
		response.setHeaders(getHeaders(connection));
		return response;
	}

	public void close() {
		// empty
	}

	private MultivaluedMap<String, String> getHeaders(final HttpURLConnection connection) {
		MultivaluedMap<String, String> headers = new CaseInsensitiveMap<String>();

		for (Entry<String, List<String>> header : connection.getHeaderFields().entrySet()) {
			if (header.getKey() != null)
				for (String value : header.getValue())
					headers.add(header.getKey(), value);
		}
		return headers;
	}

	private void outputBody(final ClientRequest request, final HttpURLConnection connection) {
		if (request.getBody() != null) {
			// System.out.println(request.getBody());
			if (connection.getRequestProperty(CONTENT_TYPE) == null) {
				String type = request.getBodyContentType().toString();
				connection.addRequestProperty(CONTENT_TYPE, type);
			}
			try {
				OutputStream os = connection.getOutputStream();
				CommitHeaderOutputStream commit = new CommitHeaderOutputStream(os,
						new CommitHeaderOutputStream.CommitCallback() {

							@Override
							public void commit() {
								commitHeaders(request, connection);
							}
						});
				request.writeRequestBody(request.getHeadersAsObjects(), commit);
				os.flush();
				os.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		} else {
			commitHeaders(request, connection);
		}
	}
}
