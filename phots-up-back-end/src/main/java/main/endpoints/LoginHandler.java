package main.endpoints;

import static org.springframework.util.StringUtils.hasText;
import java.util.function.Predicate;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import main.model.dto.LoginRequest;
import main.model.dto.TokenResponse;
import main.model.entities.User;
import main.security.TokenProvider;
import main.services.user.UserService;
import reactor.core.publisher.Mono;

@Component
public class LoginHandler {
	private final UserService userService;
	private final PasswordEncoder encoder;
	private final TokenProvider tokenProvider;
	
	public LoginHandler(UserService userService, PasswordEncoder encoder, TokenProvider tokenProvider) {
		this.userService = userService;
		this.encoder = encoder;
		this.tokenProvider = tokenProvider;
	}
	
	
	public Mono<ServerResponse> handleLogin(ServerRequest req){
		return req.bodyToMono(LoginRequest.class)
				.filter(requestNotEmpty)
				.flatMap(loginReq -> verifyCredentials(loginReq))
				.onErrorResume(err -> Mono.empty())
				.flatMap(user -> ServerResponse.ok()
						.contentType(APPLICATION_JSON)
						.body(this.tokenProvider.generateToken(user), TokenResponse.class))
				.switchIfEmpty(ServerResponse.badRequest().build());
	}
	
	
	private Predicate<LoginRequest> requestNotEmpty = loginReq ->
		hasText(loginReq.getUsername()) && hasText(loginReq.getPassword());
		

	private Mono<UserDetails> verifyCredentials(LoginRequest loginReq){
		return this.userService.findByUsername(loginReq.getUsername())
				.filter(user -> ((User) user).getConfirmCode() == null)
				.filter(user -> this.encoder.matches(loginReq.getPassword(), user.getPassword()));
	}
}