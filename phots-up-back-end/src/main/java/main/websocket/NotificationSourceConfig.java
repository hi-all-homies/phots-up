package main.websocket;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import main.model.notifications.Notification;
import reactor.core.publisher.Flux;
import reactor.core.publisher.UnicastProcessor;

@Configuration
public class NotificationSourceConfig {
	
	@Bean(name = "publisher")
	public UnicastProcessor<Notification> processor(){
		return UnicastProcessor.<Notification>create();
	}
	
	@Bean(name = "source")
	public Flux<Notification> source(@Qualifier("publisher") UnicastProcessor<Notification> processor){
		return processor.publish()
				.autoConnect(0)
				.cache(0);
	}
}
