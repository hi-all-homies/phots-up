package random.name.photsapp.services.message;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import random.name.photsapp.entities.Author;
import random.name.photsapp.entities.Chat;
import random.name.photsapp.entities.Message;
import random.name.photsapp.repositories.ChatRepo;
import random.name.photsapp.repositories.MessageRepo;
import random.name.photsapp.services.author.AuthorService;
import random.name.photsapp.services.notification.Notification;
import random.name.photsapp.services.notification.NotificationService;
import random.name.photsapp.services.notification.NotificationType;
import java.util.Comparator;
import java.util.List;
import java.util.function.Supplier;


@Service
@RequiredArgsConstructor
public class DefaultMessageService implements MessageService {

    private final ChatRepo chatRepo;

    private final MessageRepo messageRepo;

    private final AuthorService authorService;

    private final NotificationService notificationService;


    @Override
    public List<Chat> findUserChats(Author currentUser) {
        return this.chatRepo.findBySender(currentUser)
                .stream()
                .sorted(Comparator.comparing(chat -> chat.getReceiver().getUsername()))
                .toList();
    }

    @Override
    @Transactional
    public ChatMessages findChatMessages(Author currentUser, String username) {
        var receiver = this.fetchReceiver(username);

        var chat = this.chatRepo.findBySenderAndReceiver(currentUser, receiver)
                .orElseGet(createChats(currentUser, receiver));

        var messages = this.messageRepo.findByChatIdentity(chat.getChatIdentity());
        return new ChatMessages(chat, messages);
    }

    @Override
    public Message saveMessage(Author currentUser, Message message) {
        var receiver = this.fetchReceiver(message.getReceiver());

        var saved = this.messageRepo.save(message);

        this.notificationService.sendNotification(
                new Notification(NotificationType.NEW_MESSAGE, receiver, currentUser,
                    String.format("new message from %s", currentUser.getUsername())));

        return saved;
    }


    private Author fetchReceiver(String username){
        return this.authorService.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }

    private Supplier<Chat> createChats(Author currentUser, Author receiver) {
        return () -> {
            var chatId = String.format(
                    "%s_%s", currentUser.getUsername(), receiver.getUsername());

            var senderSide = Chat.builder()
                    .chatIdentity(chatId)
                    .sender(currentUser)
                    .receiver(receiver)
                    .build();

            var receiverSide = Chat.builder()
                    .chatIdentity(chatId)
                    .sender(receiver)
                    .receiver(currentUser)
                    .build();

            this.chatRepo.save(receiverSide);
            return this.chatRepo.save(senderSide);
        };
    }
}