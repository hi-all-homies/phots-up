package main.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import main.model.entities.User;
import main.model.entities.UserRole;
import reactor.test.StepVerifier;

@SpringBootTest
public class TokenProviderTest {
	@Autowired TokenProvider tokenProvider;
	@Autowired PasswordEncoder encoder;
	
	@Test
	void shouldGenerateValidToken() {
		var user = new User(134l, "token", "provider", "example@example.com");
		user.getRoles().add(UserRole.ROLE_ADMIN);
		user.getRoles().add(UserRole.ROLE_USER);
		
		var monoToken = this.tokenProvider.generateToken(user)
				.doOnNext(tokenResp -> System.out.println(tokenResp.getToken()))
				.map(tokenResp -> {
					var auth = this.tokenProvider.verifyToken(tokenResp.getToken());
					return auth.getName();
				});
		
		StepVerifier.create(monoToken)
			.expectNext("token")
			.verifyComplete();
	}
	
}
