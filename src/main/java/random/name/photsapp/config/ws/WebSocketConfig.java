package random.name.photsapp.config.ws;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.DefaultContentTypeResolver;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import random.name.photsapp.config.json.Views;
import java.util.List;


@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {


    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/user");
        registry.setApplicationDestinationPrefixes("/app");
        registry.setUserDestinationPrefix("/user");
    }


    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("http://localhost:5173")
                .withSockJS();
    }

    @Override
    public boolean configureMessageConverters(List<MessageConverter> messageConverters) {
        DefaultContentTypeResolver resolver = new DefaultContentTypeResolver();
        resolver.setDefaultMimeType(MimeTypeUtils.APPLICATION_JSON);

        var notificationMapper = new ObjectMapper();
        notificationMapper.setConfig(notificationMapper
                .getSerializationConfig().withView(Views.NotificationView.class));

        MappingJackson2MessageConverter notificationConverter = new MappingJackson2MessageConverter();
        notificationConverter.setObjectMapper(notificationMapper);
        notificationConverter.setContentTypeResolver(resolver);

        var chatMapper = new ObjectMapper();
        chatMapper.setConfig(chatMapper
                .getSerializationConfig().withView(Views.ChatView.class));

        MappingJackson2MessageConverter chatConverter = new MappingJackson2MessageConverter();
        chatConverter.setObjectMapper(chatMapper);
        chatConverter.setContentTypeResolver(resolver);

        messageConverters.add(notificationConverter);
        messageConverters.add(chatConverter);
        return false;
    }
}