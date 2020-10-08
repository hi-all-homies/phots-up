package main.endpoints;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import main.services.user.UserService;
import reactor.core.publisher.Mono;

@Component
public class ConfirmReqHandler {
	private final UserService userService;
	
	public ConfirmReqHandler(UserService userService) {
		this.userService = userService;
	}

	public Mono<ServerResponse> confirmReg(ServerRequest req){
		return Mono.just(req.pathVariable("code"))
				.map(userService::confirm)
				.flatMap(ans -> ServerResponse.ok().bodyValue(ans));
	}
}