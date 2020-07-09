package main.endpoints;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import main.facades.like.LikeFacade;
import main.model.dto.LikeRequest;
import reactor.core.publisher.Mono;

@Component
public class LikeHandler {
	private final LikeFacade likeFacade;

	public LikeHandler(LikeFacade likeFacade) {
		this.likeFacade = likeFacade;
	}
	
	public Mono<ServerResponse> handleLikeRequest(ServerRequest req){
		return req.bodyToMono(LikeRequest.class)
				.flatMap(likeFacade::addLike)
				.flatMap(result -> ServerResponse.ok()
						.bodyValue(result));
	}
	
}
