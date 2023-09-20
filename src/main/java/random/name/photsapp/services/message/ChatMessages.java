package random.name.photsapp.services.message;

import com.fasterxml.jackson.annotation.JsonView;
import random.name.photsapp.config.json.Views;
import random.name.photsapp.entities.Chat;
import random.name.photsapp.entities.Message;
import java.util.List;

@JsonView(Views.ChatView.class)
public record ChatMessages (Chat chat, List<Message> messages) {}