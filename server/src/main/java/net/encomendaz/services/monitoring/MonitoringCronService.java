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
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import net.encomendaz.services.Response;
import net.encomendaz.services.tracking.TrackingManager;

@Path("/monitoring.cron")
@Produces(MEDIA_TYPE)
public class MonitoringCronService {

	@GET
	public Response<String> execute() {
		String hash;
		Date date;

		for (Monitoring monitoring : MonitoringManager.x()) {
			date = new Date();
			hash = TrackingManager.hash(monitoring.getTrackId());

			if (!monitoring.getHash().equals(hash)) {
				monitoring.setHash(hash);
				monitoring.setUpdated(date);

				mail(monitoring);
			}

			monitoring.setMonitored(date);
			MonitoringManager.update(monitoring);
		}

		Response<String> response = new Response<String>();
		response.setStatus(OK);

		return response;
	}

	private void mail(Monitoring monitoring) {
		try {
			Properties props = new Properties();
			Session session = Session.getDefaultInstance(props, null);

			String msgBody = "O status da encomenda " + monitoring.getTrackId() + " mudou!";

			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress("cleverson.sacramento@gmail.com"));
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(monitoring.getClientId()));
			msg.setSubject(msgBody);
			msg.setText(msgBody);
			Transport.send(msg);

		} catch (Exception cause) {
			throw new RuntimeException(cause);
		}
	}
}
