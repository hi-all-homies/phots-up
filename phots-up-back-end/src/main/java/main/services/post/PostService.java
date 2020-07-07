package main.services.post;

import main.model.dto.PostSummary;
import main.model.entities.Post;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PostService {
	
	public Flux<PostSummary> getAllPosts(int page);
	
	public Mono<PostSummary> getPostById(Long postId);
	
	public Mono<Post> savePost(Post post);
	
	public Mono<Integer> updatePost(Post post);
	
	public Mono<Void> deletePost(Post post);
}
