package main.websocket;

import java.util.function.Predicate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import main.model.notifications.Notification;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class NotificationHandler implements WebSocketHandler {
	private final Flux<Notification> source;

	public NotificationHandler(@Qualifier("source") Flux<Notification> source) {
		this.source = source;
	}

	@Override
	public Mono<Void> handle(WebSocketSession session) {
		var username = session.getHandshakeInfo()
				.getHeaders()
				.getFirst("username");
		
		var notifications = this.source
				.filter(filterNotifications(username))
				.map(item -> item.turnIntoJsonString())
				.map(session::textMessage);
		
		return session.send(notifications);
	}
	
	private Predicate<Notification> filterNotifications(String username){ 
			return item -> item.getReceiver().equals(username);
	}
}
