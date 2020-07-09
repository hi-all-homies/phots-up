package main.facades.like;

import main.model.dto.LikeRequest;
import reactor.core.publisher.Mono;

public interface LikeFacade {
	
	public Mono<Boolean> addLike(LikeRequest req);
}
