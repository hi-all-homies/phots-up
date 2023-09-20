package random.name.photsapp.services.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SocketNotificationService implements NotificationService {

    private final SimpMessagingTemplate messaging;

    @Override
    @Async
    public void sendNotification(Notification notification) {
        messaging.convertAndSendToUser(
                notification.receiver().getUsername(),
                "/que/events",
                notification);
    }
}