package main.endpoints;

import static org.springframework.util.StringUtils.hasText;
import java.util.function.Predicate;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.TEXT_PLAIN;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import main.model.dto.LoginRequest;
import main.model.entities.User;
import main.services.user.UserService;
import reactor.core.publisher.Mono;

@Component
public class RegistrationHandler {
	private final UserService userService;

	public RegistrationHandler(UserService userService) {
		this.userService = userService;
	}
	
	public Mono<ServerResponse> handleRegistration(ServerRequest req){
		return req.bodyToMono(LoginRequest.class)
				.filter(requestNotEmpty)
				.map(this::mapToUser)
				.flatMap(userService::registerUser)
				.flatMap(this::generateResponse)
				.switchIfEmpty(generateResponse(false));
	}
	
	private Mono<ServerResponse> generateResponse(boolean userIsSaved){
		if (userIsSaved)
			return ServerResponse.status(CREATED)
					.build();
		else
			return ServerResponse.badRequest()
					.contentType(TEXT_PLAIN)
					.bodyValue("that username already exist or empty fields");
	}
	
	private Predicate<LoginRequest> requestNotEmpty = loginReq ->
		hasText(loginReq.getUsername()) && hasText(loginReq.getPassword());
	
	private User mapToUser(LoginRequest request) {
		return new User(null, request.getUsername(), request.getPassword());
	}
}
