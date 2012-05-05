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
package net.encomendaz.services.monitoring;

import static net.encomendaz.services.Response.MEDIA_TYPE;
import static net.encomendaz.services.Response.Status.OK;

import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import net.encomendaz.services.Response;
import net.encomendaz.services.notification.Aps;
import net.encomendaz.services.notification.MyClientExecutor;
import net.encomendaz.services.notification.Push;
import net.encomendaz.services.notification.NotificationProxy;
import net.encomendaz.services.tracking.Tracking;
import net.encomendaz.services.tracking.TrackingManager;

import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.jboss.resteasy.client.ProxyFactory;
import org.jboss.resteasy.spi.ResteasyProviderFactory;

@Path("/monitoring.cron")
@Produces(MEDIA_TYPE)
public class MonitoringCronService {

	@GET
	public Response<String> execute() throws Exception {
		String hash;
		Date date;
		Tracking tracking;
		Exception exception = null;

		for (Monitoring monitoring : MonitoringManager.findAll()) {
			try {
				date = new Date();
				tracking = TrackingManager.search(monitoring.getTrackId());
				hash = tracking.getHash();

				if (!monitoring.getHash().equals(hash)) {
					monitoring.setHash(hash);
					monitoring.setUpdated(date);

					notify(monitoring);
				}

				if (tracking.isCompleted()) {
					MonitoringManager.delete(monitoring);

				} else {
					monitoring.setMonitored(date);
					MonitoringManager.update(monitoring);
				}

			} catch (Exception cause) {
				exception = cause;
			}
		}

		if (exception != null) {
			throw exception;
		}

		Response<String> response = new Response<String>();
		response.setStatus(OK);

		return response;
	}

	private void notify(Monitoring monitoring) throws Exception {
		if (monitoring.getClientId().indexOf("@") > 0) {
			mail(monitoring);

		} else {
			push(monitoring);
		}
	}

	private void mail(Monitoring monitoring) throws AddressException, MessagingException {
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

	static {
		ResteasyProviderFactory.setRegisterBuiltinByDefault(false);
		ResteasyProviderFactory.getInstance().registerProvider(JacksonJsonProvider.class);
	}

	private void push(Monitoring monitoring) {
		// ResourceBundle bundle = ResourceBundle.getBundle("encomendaz-server");
		// String username = bundle.getString("airhsip-username");
		// String password = bundle.getString("airhsip-password");

		// DefaultHttpClient httpClient = new DefaultHttpClient();
		// Credentials credentials = new UsernamePasswordCredentials(username, password);
		// httpClient.getCredentialsProvider().setCredentials(org.apache.http.auth.AuthScope.ANY, credentials);

		// ClientRequest request = new ClientRequest(password);

		// ClientExecutor clientExecutor = new ApacheHttpClient4Executor(httpClient);

		Aps aps = new Aps();
		aps.setAlert("O status da encomenda " + monitoring.getTrackId() + " mudou!");
		aps.setSound("default");
		// aps.setBadge("+1");

		Push push = new Push();
		push.addAlias(monitoring.getClientId());
		push.setAps(aps);

		NotificationProxy proxy = ProxyFactory.create(NotificationProxy.class, "https://go.urbanairship.com",
				new MyClientExecutor());
		proxy.notify(push);
	}
}
