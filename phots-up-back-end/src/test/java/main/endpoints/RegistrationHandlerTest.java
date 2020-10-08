package main.endpoints;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import main.model.dto.LoginRequest;
import main.model.entities.User;
import main.services.user.UserService;
import reactor.core.publisher.Mono;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class RegistrationHandlerTest {
	@Autowired WebTestClient client;
	@MockBean UserService userService;
	
	
	@Test
	void shouldRegUserWithNewUsernameAndViceVersa() {
		var newUser = new LoginRequest("new-username", "12345", null);
		
		var existedUser = new LoginRequest("existed", "12345", null);
		
		Mockito.when(userService.registerUser(new User(null, newUser.getUsername(), newUser.getPassword(), "example@example.com")))
			.thenReturn(Mono.just(true));
		
		Mockito.when(userService.registerUser(new User(null, existedUser.getUsername(), existedUser.getPassword(), "example@example.com")))
			.thenReturn(Mono.just(false));
		
		client.post().uri("/phots/up/api/signup")
			.accept(MediaType.APPLICATION_JSON)
			.bodyValue(newUser)
			.exchange()
			.expectStatus().isCreated();
		
		
		client.post().uri("/phots/up/api/signup")
		.accept(MediaType.APPLICATION_JSON)
		.bodyValue(existedUser)
		.exchange()
		.expectStatus().isBadRequest()
		.expectBody(String.class).consumeWith(result -> System.out.println(result.getResponseBody()));
	}
}
