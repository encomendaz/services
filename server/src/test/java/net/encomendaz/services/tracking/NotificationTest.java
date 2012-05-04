package net.encomendaz.services.tracking;

import java.util.ResourceBundle;

import net.encomendaz.services.notification.Aps;
import net.encomendaz.services.notification.Notification;
import net.encomendaz.services.notification.NotificationService;

import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.DefaultHttpClient;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.jboss.resteasy.client.ClientExecutor;
import org.jboss.resteasy.client.ProxyFactory;
import org.jboss.resteasy.client.core.executors.ApacheHttpClient4Executor;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.junit.Before;
import org.junit.Test;

public class NotificationTest {

	@Before
	public void before() {
		ResteasyProviderFactory.setRegisterBuiltinByDefault(false);
		ResteasyProviderFactory.getInstance().registerProvider(JacksonJsonProvider.class);
	}

	@Test
	public void push() {
		ResourceBundle bundle = ResourceBundle.getBundle("encomendaz-server");
		String username = bundle.getString("airhsip-username");
		String password = bundle.getString("airhsip-password");

		DefaultHttpClient httpClient = new DefaultHttpClient();
		Credentials credentials = new UsernamePasswordCredentials(username, password);
		httpClient.getCredentialsProvider().setCredentials(org.apache.http.auth.AuthScope.ANY, credentials);
		ClientExecutor clientExecutor = new ApacheHttpClient4Executor(httpClient);

		NotificationService service = ProxyFactory.create(NotificationService.class, "https://go.urbanairship.com",
				clientExecutor);

		Aps aps = new Aps();
		aps.setAlert("opa opa");
		aps.setSound("default");
		//aps.setBadge("+1");

		Notification notification = new Notification();
		notification.addAlias("4047646072210958685725219983627208515076714460526118401538638053");
		notification.setAps(aps);

		service.notify(notification);
	}
}
