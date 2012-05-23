package net.encomendaz.services.tracking;

import net.encomendaz.services.notification.AirshipClientExecutor;
import net.encomendaz.services.notification.Registration;
import net.encomendaz.services.notification.RegistrationProxy;

import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ProxyFactory;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.junit.Test;

public class RegistrationTest {

	@Test
	public void registration() {
		ResteasyProviderFactory.setRegisterBuiltinByDefault(false);
		ResteasyProviderFactory.getInstance().registerProvider(JacksonJsonProvider.class);
		ClientRequest.setDefaultExecutorClass(AirshipClientExecutor.class.getCanonicalName());
		
		String clientId = "00538051640715478661951795980173";
		String deviceToken = "442d6f5719a107393069bfdc0007d4787cd9fc3d6cc95d16c9cfa45da7bfeb0c";

		Registration registration = new Registration();
		registration.setAlias(clientId);

		RegistrationProxy proxy = ProxyFactory.create(RegistrationProxy.class, "https://go.urbanairship.com");
		proxy.register(deviceToken, registration);
	}
}
