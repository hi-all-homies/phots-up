package main.services.post;

import main.model.dto.PostSummary;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PostService {
	
	public Flux<PostSummary> getAllPosts(int page);
	
	public Mono<PostSummary> getPostById(Long postId);
}
