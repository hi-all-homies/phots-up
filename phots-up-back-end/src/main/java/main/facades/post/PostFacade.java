package main.facades.post;

import org.springframework.http.codec.multipart.Part;
import org.springframework.util.MultiValueMap;
import main.model.dto.PostSummary;
import main.model.entities.Post;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PostFacade {
	
	public Mono<Post> storePost(MultiValueMap<String, Part> data);
	
	public Flux<PostSummary>  getPosts(int page);
	
	public Mono<PostSummary> getPostById(Long postId);
}
