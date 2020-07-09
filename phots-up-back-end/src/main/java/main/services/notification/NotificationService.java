package main.services.notification;

import main.model.notifications.Notification;

public interface NotificationService {
	
	public void sendNotification(Notification notification);
}
