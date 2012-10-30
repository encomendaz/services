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

import java.net.HttpURLConnection;
import java.util.ResourceBundle;

import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.core.executors.URLConnectionClientExecutor;
import org.jboss.resteasy.util.Base64;

public class AirshipClientExecutor extends URLConnectionClientExecutor {

	@Override
	protected HttpURLConnection createConnection(ClientRequest request) throws Exception {
		ResourceBundle bundle = ResourceBundle.getBundle("encomendaz-server");
		String appKey = bundle.getString("airship.username");
		String appMasterSecret = bundle.getString("airship.password");

		String authString = appKey + ":" + appMasterSecret;
		String authStringBase64 = Base64.encodeBytes(authString.getBytes());
		authStringBase64 = authStringBase64.trim();

		HttpURLConnection connection = super.createConnection(request);
		connection.addRequestProperty("Authorization", "Basic " + authStringBase64);

		return connection;
	}
}
