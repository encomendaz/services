package net.encomendaz.services.notification;

import static net.encomendaz.services.Response.MEDIA_TYPE;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Path("/api/push/")
@Consumes(MEDIA_TYPE)
public interface NotificationService {

	@POST
	public void notify(Notification notification);

}
