package main.websocket;

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
		var noti = source.map(notific -> notific.turnIntoJsonString())
			.map(session::textMessage);
		
		return session.send(noti);
	}
	
	
}
