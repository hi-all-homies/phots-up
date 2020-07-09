package main.model.notifications;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class Notification {
	private final NotificationType type;
	private final String receiver;
	
	public abstract String turnIntoJsonString();
}
