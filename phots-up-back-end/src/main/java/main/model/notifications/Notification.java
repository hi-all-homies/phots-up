package main.model.notifications;

public abstract class Notification {
	private NotificationType type;
	
	public NotificationType getType() {
		return this.type;
	}
}
