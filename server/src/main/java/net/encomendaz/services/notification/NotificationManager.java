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
import net.encomendaz.services.tracking.Trace;
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
		ClientRequest.setDefaultExecutorClass(AirshipClientExecutor.class.getCanonicalName());
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
		Aps aps = new Aps();
		aps.setAlert(buildMessage(monitoring, tracking));
		aps.setSound("default");
		// aps.setBadge("+1");

		Push push = new Push();
		push.addAlias(monitoring.getClientId());
		push.setAps(aps);

		NotificationProxy proxy = ProxyFactory.create(NotificationProxy.class, "https://go.urbanairship.com");
		proxy.notify(push);
	}

	private static String buildMessage(Monitoring monitoring, Tracking tracking) {
		StringBuffer message = new StringBuffer();

		message.append("A encomenda ");
		message.append(monitoring.getTrackId());

		if (!Strings.isEmpty(monitoring.getLabel())) {
			message.append(" (" + monitoring.getLabel() + ")");
		}
		
		message.append(" ");

		switch (tracking.getLastTrace().getStatus()) {
			case ACCEPTANCE:
				message.append("foi postada");
				message.append(buildCity(tracking.getLastTrace()));

				break;
			case ENROUTE:
				message.append("foi encaminhada para o próximo destino");

				break;
			case CHECKED:
				message.append("foi verificada");
				message.append(buildCity(tracking.getLastTrace()));

				break;

			case DELIVERING:
				message.append("saiu para entrega");
				message.append(buildCity(tracking.getLastTrace()));

				break;

			case DELIVERED:
				message.append("foi entregue");
				message.append(buildCity(tracking.getLastTrace()));

				break;

			case AWAITING:
				message.append("está aguardando retirada");
				message.append(buildCity(tracking.getLastTrace()));

				break;

			default:
				message.append("está com um status desconhecido");
		}
		
		message.append(".");

		return message.toString();
	}

	private static String buildCity(Trace trace) {
		StringBuffer city = new StringBuffer();

		if (!Strings.isEmpty(trace.getCity())) {
			city.append(" em " + trace.getCity());

			if (!Strings.isEmpty(trace.getState())) {
				city.append("/" + trace.getState());
			}
		}

		return city.toString();
	}
}
