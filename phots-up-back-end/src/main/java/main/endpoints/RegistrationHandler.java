package main.endpoints;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.util.StringUtils.hasText;
import java.util.regex.Pattern;
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
	private final Pattern pattern;
	
	public RegistrationHandler(UserService userService) {
		this.userService = userService;
		this.pattern = Pattern.compile("^(.+)@(.+)$");
	}
	
	public Mono<ServerResponse> handleRegistration(ServerRequest req){
		return req.bodyToMono(LoginRequest.class)
				.filter(this::checkRequest)
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
					.bodyValue("that username already exist or empty fields");
	}
	
	private boolean checkRequest(LoginRequest loginReq) {
		var email = loginReq.getEmail();
		boolean matchResult =  email != null ? this.pattern.matcher(email).matches() : false;
		
		return hasText(loginReq.getUsername()) &&
				hasText(loginReq.getPassword()) &&
				matchResult;
	}
		
	private User mapToUser(LoginRequest request) {
		return new User(null, request.getUsername(), request.getPassword(), request.getEmail());
	}
}