package random.name.photsapp.resource;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import random.name.photsapp.config.json.Views;
import random.name.photsapp.entities.Chat;
import random.name.photsapp.entities.Message;
import random.name.photsapp.services.author.AuthorDetails;
import random.name.photsapp.services.message.ChatMessages;
import random.name.photsapp.services.message.MessageService;
import java.security.Principal;
import java.util.List;
import static random.name.photsapp.services.author.AuthorDetails.ROLE_USER;


@RestController
@RequiredArgsConstructor
public class MessageEndpoint {

    private final MessageService messageService;

    private final SimpMessagingTemplate messaging;


    @MessageMapping("/chat")
    public void sendMessage(@Payload @Valid Message message, Principal principal){

        var authToken = (UsernamePasswordAuthenticationToken) principal;
        var authorDetails = (AuthorDetails) authToken.getPrincipal();

        var saved = this.messageService.saveMessage(authorDetails.getAuthor(), message);

        this.messaging.convertAndSendToUser(
                saved.getChatIdentity(),
                "/messages",
                saved
        );
    }


    @Secured(ROLE_USER)
    @JsonView(Views.WebSocketMessage.class)
    @GetMapping("/chats")
    public ResponseEntity<List<Chat>> getUserChats(@AuthenticationPrincipal AuthorDetails user){
        return ResponseEntity.ok(
                this.messageService.findUserChats(user.getAuthor()));
    }


    @Secured(ROLE_USER)
    @JsonView(Views.WebSocketMessage.class)
    @GetMapping("/chats/{username}")
    public ResponseEntity<ChatMessages> getChatMessages(
            @AuthenticationPrincipal AuthorDetails user,
            @PathVariable("username") String username){

        return ResponseEntity.ok(
                this.messageService.findChatMessages(user.getAuthor(), username));
    }
}