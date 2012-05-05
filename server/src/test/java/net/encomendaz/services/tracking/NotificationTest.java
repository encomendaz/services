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
package net.encomendaz.services.tracking;

import net.encomendaz.services.notification.Aps;
import net.encomendaz.services.notification.MyClientExecutor;
import net.encomendaz.services.notification.Push;
import net.encomendaz.services.notification.NotificationProxy;

import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.jboss.resteasy.client.ProxyFactory;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class NotificationTest {

	@Before
	public void before() {
		ResteasyProviderFactory.setRegisterBuiltinByDefault(false);
		ResteasyProviderFactory.getInstance().registerProvider(JacksonJsonProvider.class);
	}

	@Test
	@Ignore
	public void push() {
		// ResourceBundle bundle = ResourceBundle.getBundle("encomendaz-server");
		// String username = bundle.getString("airhsip-username");
		// String password = bundle.getString("airhsip-password");
		//
		// DefaultHttpClient httpClient = new DefaultHttpClient();
		// Credentials credentials = new UsernamePasswordCredentials(username, password);
		// httpClient.getCredentialsProvider().setCredentials(org.apache.http.auth.AuthScope.ANY, credentials);
		// ClientExecutor clientExecutor = new ApacheHttpClient4Executor(httpClient);

		Aps aps = new Aps();
		aps.setAlert("opa opa");
		aps.setSound("default");
		// aps.setBadge("+1");

		Push push = new Push();
		push.addAlias("4047646072210958685725219983627208515076714460526118401538638053");
		push.setAps(aps);

		NotificationProxy proxy = ProxyFactory.create(NotificationProxy.class, "https://go.urbanairship.com",
				new MyClientExecutor());
		proxy.notify(push);
	}
}
