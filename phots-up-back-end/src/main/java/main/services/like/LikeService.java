package main.services.like;

import main.model.dto.LikeRequest;
import reactor.core.publisher.Mono;

interface LikeService {
	
	public Mono<Boolean> addLike(LikeRequest likeReq);
}
