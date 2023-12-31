package main.endpoints;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import main.facades.user.UserInfoFacade;
import main.model.entities.User;
import reactor.core.publisher.Mono;

@Component
public class UserInfoHandler {
	private final UserInfoFacade userInfoFacade;

	public UserInfoHandler(UserInfoFacade userInfoFacade) {
		this.userInfoFacade = userInfoFacade;
	}
	
	public Mono<ServerResponse> getUserInfo(ServerRequest req){
		var userId = Long.valueOf(req.pathVariable("userId"));
		return ServerResponse.ok()
				.contentType(APPLICATION_JSON)
				.body(this.userInfoFacade.getUserInfoByUserId(userId), User.class);
	}
	
	public Mono<ServerResponse> saveUserInfo(ServerRequest req){
		final var token = req.cookies().getFirst("token").getValue();
		
		return req.multipartData()
				.flatMap(data -> userInfoFacade.setOrUpdateUserInfo(data, token))
				.flatMap(user -> ServerResponse.ok()
						.contentType(APPLICATION_JSON)
						.bodyValue(user));
	}
}
