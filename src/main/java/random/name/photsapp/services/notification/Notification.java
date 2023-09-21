package random.name.photsapp.services.notification;

import com.fasterxml.jackson.annotation.JsonView;
import random.name.photsapp.config.json.Views;
import random.name.photsapp.entities.Author;
import java.io.Serializable;


@JsonView(Views.WebSocketMessage.class)
public record Notification(

        NotificationType type, Author receiver,
        Author causer, String reason

)implements Serializable {}