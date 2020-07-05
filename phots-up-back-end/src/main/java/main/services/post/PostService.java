package main.services.post;

import main.model.dto.PostSummary;
import reactor.core.publisher.Flux;

public interface PostService {
	
	public Flux<PostSummary> getAllPosts(int page);
}
