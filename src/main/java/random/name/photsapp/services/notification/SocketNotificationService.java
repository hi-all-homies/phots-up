package random.name.photsapp.services.notification;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import random.name.photsapp.config.json.Views;

@Service
public class SocketNotificationService implements NotificationService {

    private final SimpMessagingTemplate messaging;

    public SocketNotificationService(SimpMessagingTemplate messaging, ObjectMapper mapper) {
        this.messaging = messaging;
        messaging.setMessageConverter(new NotificationConverter(mapper));
    }

    @Override
    @Async
    public void sendNotification(Notification notification) {
        messaging.convertAndSendToUser(
                notification.receiver().getUsername(),
                "/que/events",
                notification);
    }


    record NotificationConverter(ObjectMapper mapper) implements MessageConverter {
            public Object fromMessage(Message<?> message, Class<?> targetClass) {
                return null;
            }

            public Message<?> toMessage(Object payload, MessageHeaders headers) {
                try {
                    var body = this.mapper.writerWithView(Views.NotificationView.class)
                            .writeValueAsBytes(payload);

                    return new NotificationMessage(body, headers);

                } catch (JsonProcessingException ex) {
                    throw new RuntimeException(ex.getMessage());
                }
            }
        }

    record NotificationMessage(byte[] payload, MessageHeaders headers) implements Message<byte[]> {
        public byte[] getPayload() {
            return this.payload;
        }

        public MessageHeaders getHeaders() {
            return this.headers;
        }
    }
}