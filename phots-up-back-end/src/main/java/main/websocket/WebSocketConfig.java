package main.websocket;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.server.support.HandshakeWebSocketService;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;
import main.model.notifications.Notification;
import reactor.core.publisher.Flux;

@Configuration
public class WebSocketConfig {
	@Autowired
	@Qualifier("source")
	private Flux<Notification> source;
	
	@Autowired
	private CustomReqUpgradeStrategy upgradeStrategy;
	
	@Bean
	public HandlerMapping webSocketMapping() {
		Map<String, WebSocketHandler>handlers = new HashMap<>();
		handlers.put("/photsup/notifications", new NotificationHandler(source));
		
		var mapping = new SimpleUrlHandlerMapping();
		mapping.setUrlMap(handlers);
		mapping.setCorsConfigurationSource(cors());
		mapping.setOrder(-1);
		return mapping;
	}
	
	
	@Bean
	public HandshakeWebSocketService handshakeService() {
		return new HandshakeWebSocketService(this.upgradeStrategy);
	}
	
	@Bean
	public WebSocketHandlerAdapter adapter() {
		return new WebSocketHandlerAdapter(handshakeService());
	}
	
	@Bean
	public CorsConfigurationSource cors() {
		var config = new CorsConfiguration();
		config.setAllowedHeaders(Arrays.asList("*"));
		config.setAllowedMethods(Arrays.asList("*"));
		config.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
		
		UrlBasedCorsConfigurationSource source =
				new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		return source;
	}
}
