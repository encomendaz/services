package net.encomendaz.services.notification;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import net.encomendaz.services.monitoring.Monitoring;
import net.encomendaz.services.tracking.Tracking;
import net.encomendaz.services.util.Strings;

import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ProxyFactory;
import org.jboss.resteasy.spi.ResteasyProviderFactory;

public class NotificationManager {

	static {
		ResteasyProviderFactory.setRegisterBuiltinByDefault(false);
		ResteasyProviderFactory.getInstance().registerProvider(JacksonJsonProvider.class);
		ClientRequest.setDefaultExecutorClass(MyClientExecutor.class.getCanonicalName());
	}

	public static void send(Monitoring monitoring, Tracking tracking) throws Exception {
		if (monitoring.getClientId().indexOf("@") > 0) {
			mail(monitoring, tracking);

		} else {
			push(monitoring, tracking);
		}
	}

	private static void mail(Monitoring monitoring, Tracking tracking) throws AddressException, MessagingException {
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);

		String msgBody = "O status da encomenda " + monitoring.getTrackId() + " mudou!";

		Message msg = new MimeMessage(session);
		msg.setFrom(new InternetAddress("cleverson.sacramento@gmail.com"));
		msg.addRecipient(Message.RecipientType.TO, new InternetAddress(monitoring.getClientId()));
		msg.setSubject(msgBody);
		msg.setText(msgBody);
		Transport.send(msg);
	}

	private static void push(Monitoring monitoring, Tracking tracking) {
		String label = Strings.isEmpty(monitoring.getLabel()) ? "" : " (" + monitoring.getLabel() + ")";
		String alert = "O status da encomenda " + monitoring.getTrackId() + label + " mudou para "
				+ tracking.getLastTrace().getStatus();

		Aps aps = new Aps();
		aps.setAlert(alert);
		aps.setSound("default");
		// aps.setBadge("+1");

		Push push = new Push();
		push.addAlias(monitoring.getClientId());
		push.setAps(aps);

		NotificationProxy proxy = ProxyFactory.create(NotificationProxy.class, "https://go.urbanairship.com");
		proxy.notify(push);
	}
}
