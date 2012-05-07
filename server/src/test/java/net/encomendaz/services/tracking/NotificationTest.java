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
import net.encomendaz.services.notification.NotificationProxy;
import net.encomendaz.services.notification.Push;

import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.jboss.resteasy.client.ClientRequest;
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
		ClientRequest.setDefaultExecutorClass(MyClientExecutor.class.getCanonicalName());
	}

	@Test
	@Ignore
	public void push() {
		Aps aps = new Aps();
		aps.setAlert("opa opa 2");
		aps.setSound("default");
		// aps.setBadge("+1");

		Push push = new Push();
		push.addAlias("1340967452355975009635683525851411075792113605293178132545534717");
		push.setAps(aps);

		NotificationProxy proxy = ProxyFactory.create(NotificationProxy.class, "https://go.urbanairship.com");
		proxy.notify(push);
	}
}
