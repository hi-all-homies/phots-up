package main.endpoints;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import main.model.dto.LoginRequest;
import main.model.dto.TokenResponse;
import main.model.entities.User;
import main.model.entities.UserRole;
import main.services.user.UserService;
import reactor.core.publisher.Mono;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class LoginHandlerTest {
	@Autowired WebTestClient client;
	@MockBean UserService userService;
	@MockBean PasswordEncoder encoder;
	
	
	@Test
	void shouldGenerateJWT() {
		var user = new User(23l, "donald", "12345", "example@example.com");
		user.getRoles().add(UserRole.ROLE_USER);
		
		LoginRequest lr = new LoginRequest("donald", "12345", null);
		
		Mockito.when(userService.findByUsername(lr.getUsername()))
			.thenReturn(Mono.just(user));
		
		Mockito.when(encoder.matches(lr.getPassword(), user.getPassword())).thenReturn(true);
		
		var geneatedToken = this.client.post().uri("/phots/up/api/login")
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON)
			.body(BodyInserters.fromValue(lr))
			.exchange()
			.expectStatus().isOk()
			.expectBody(TokenResponse.class)
			.returnResult()
			.getResponseBody().getToken();
		
		System.out.println(geneatedToken);
	}
}
