package random.name.photsapp.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import random.name.photsapp.entities.Author;
import random.name.photsapp.entities.Chat;
import random.name.photsapp.entities.Message;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ChatRepoTest {
    @Autowired TestEntityManager manager;
    @Autowired ChatRepo chatRepo;
    @Autowired MessageRepo messageRepo;

    Author world;
    Author hello;

    @BeforeEach
    void setUp(){
        world = new Author();
        world.setUsername("World");
        world.setPassword("password123");
        world.setAvatarUrl("ava.jpeg");

        hello = new Author();
        hello.setUsername("Hello");
        hello.setPassword("password123");
        hello.setAvatarUrl("ava.jpeg");

        manager.persist(world);
        manager.persist(hello);

        var chatId = String.format("%s_%s",
                world.getUsername(), hello.getUsername());

        var forSender = Chat.builder()
                .sender(world)
                .receiver(hello)
                .chatIdentity(chatId)
                .build();

        var forReceiver = Chat.builder()
                .sender(hello)
                .receiver(world)
                .chatIdentity(chatId)
                .build();

        manager.persist(forSender);
        manager.persist(forReceiver);

        var message1 = Message.builder()
                .chatIdentity(chatId)
                .sender(world.getUsername())
                .receiver(hello.getUsername())
                .payload("hello there!")
                .build();
        manager.persist(message1);

        var message2 = Message.builder()
                .chatIdentity(chatId)
                .sender(hello.getUsername())
                .receiver(world.getUsername())
                .payload("bye?")
                .build();
        manager.persist(message2);
    }


    @Test
    public void should_just_work(){
        var chats = chatRepo.findBySender(hello);
        assertEquals(1, chats.size());
        var chat = chats.get(0);
        assertEquals("World", chat.getReceiver().getUsername());


        var optChat = chatRepo.findBySenderAndReceiver(world, hello);
        assertTrue(optChat.isPresent());
        assertEquals("Hello", optChat.get().getReceiver().getUsername());

        var chatId = optChat.get().getChatIdentity();
        var messages = messageRepo.findByChatIdentity(chatId);
        assertEquals(2, messages.size());

        assertTrue(messages.stream().allMatch(
                message -> message.getChatIdentity().equals(chatId)
        ));
    }
}