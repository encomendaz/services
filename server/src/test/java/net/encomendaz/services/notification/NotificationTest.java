package net.encomendaz.services.notification;

import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ProxyFactory;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.junit.Test;

public class NotificationTest {

	@Test
	public void send() {
		ResteasyProviderFactory.setRegisterBuiltinByDefault(false);
		ResteasyProviderFactory.getInstance().registerProvider(JacksonJsonProvider.class);
		ClientRequest.setDefaultExecutorClass(AirshipClientExecutor.class.getCanonicalName());

		Aps aps = new Aps();
		aps.setAlert("Olá!");
		aps.setSound("default");
		// aps.setBadge("+1");

		Push push = new Push();
		// push.addAlias("86543654199261380431726829184927");
		push.addAlias("91448300404063076307502904506675");
		push.setAps(aps);

		NotificationProxy proxy = ProxyFactory.create(NotificationProxy.class, "https://go.urbanairship.com");
		proxy.notify(push);
	}
}
