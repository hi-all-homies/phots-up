package main.endpoints;

import static org.springframework.util.StringUtils.hasText;
import java.time.Duration;
import java.util.function.Function;
import java.util.function.Predicate;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import main.model.dto.LoginRequest;
import main.model.dto.LoginSuccessResponse;
import main.model.entities.User;
import main.security.TokenProvider;
import main.services.user.UserService;
import reactor.core.publisher.Mono;

@Component
public class LoginHandler {
	private final UserService userService;
	private final PasswordEncoder encoder;
	private TokenProvider tokenProvider;
	
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
				.flatMap(sendSuccessLogin)
				.switchIfEmpty(ServerResponse.badRequest().build());
	}
	
	
	private Function<UserDetails, Mono<ServerResponse>> sendSuccessLogin = user -> {
		var token = this.tokenProvider.generateToken(user);
		
		var cookie = ResponseCookie.from("token", token)
				.maxAge(Duration.ofDays(1)).httpOnly(true)
				.path("/").domain("").sameSite("Lax")
				.build();
		
		var castedUser = (User) user;
		var userInfo = castedUser.getUserInfo();
		var successResp = LoginSuccessResponse.builder()
				.userId(castedUser.getId()).username(castedUser.getUsername())
				.avatarUrl((userInfo != null) ? userInfo.getAvatarUrl() : null)
				.build();
		
		return ServerResponse.ok()
				.contentType(APPLICATION_JSON)
				.cookie(cookie)
				.bodyValue(successResp);
	};
	
	
	private Predicate<LoginRequest> requestNotEmpty = loginReq ->
		hasText(loginReq.getUsername()) && hasText(loginReq.getPassword());
		

	private Mono<UserDetails> verifyCredentials(LoginRequest loginReq){
		return this.userService.findByUsername(loginReq.getUsername())
				.filter(user -> ((User) user).getConfirmCode() == null)
				.filter(user -> this.encoder.matches(loginReq.getPassword(), user.getPassword()));
	}
}