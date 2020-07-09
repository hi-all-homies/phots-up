package main.services.notification;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import main.model.notifications.Notification;
import reactor.core.publisher.UnicastProcessor;

@Service
public class NotificationServiceImpl implements NotificationService{
	private final UnicastProcessor<Notification> publisher;
	
	public NotificationServiceImpl(@Qualifier("publisher") UnicastProcessor<Notification> publisher) {
		this.publisher = publisher;
	}

	@Override
	public void sendNotification(Notification notification) {
		this.publisher.onNext(notification);
	}
	
}
