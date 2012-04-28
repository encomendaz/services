package net.encomendaz.services.notification;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;

//public class NotificationManager extends DAOBase {
public class NotificationManager {

	static {
		ObjectifyService.register(Notification.class);
	}

	public static void ops(Notification notification) {
		Objectify objectify = ObjectifyService.begin();
		objectify.put(notification);
	}
}
