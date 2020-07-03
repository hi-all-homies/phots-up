package main.model.notifications;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class Notification {
	private final NotificationType type;
	
	public abstract String turnIntoJsonString();
}
