package random.name.photsapp.services.message;

import random.name.photsapp.entities.Author;
import random.name.photsapp.entities.Chat;
import random.name.photsapp.entities.Message;
import java.util.List;

public interface MessageService {

    List<Chat> findUserChats(Author currentUser);

    ChatMessages findChatMessages(Author currentUser,String chatWith);

    Message saveMessage(Author currentUser, Message message);
}