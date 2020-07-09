package main.endpoints;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import main.facades.post.PostFacade;
import main.model.dto.PostSummary;
import main.model.entities.Post;
import reactor.core.publisher.Mono;

@Component
public class PostHandler {
	private final PostFacade postFacade;

	public PostHandler(PostFacade postFacade) {
		this.postFacade = postFacade;
	}
	
	public Mono<ServerResponse> getAllPosts(ServerRequest req){
		var page = req.queryParam("page").get();
		var fluxPosts = this.postFacade.getPosts(Integer.valueOf(page));
		
		return ServerResponse.ok()
				.contentType(MediaType.APPLICATION_STREAM_JSON)
				.body(fluxPosts, PostSummary.class);
	}
	
	public Mono<ServerResponse> savePost(ServerRequest req){
		return req.multipartData()
				.flatMap(postFacade::storePost)
				.flatMap(post -> ServerResponse.ok()
						.contentType(MediaType.TEXT_PLAIN)
						.bodyValue("saved with id " + post.getId()));
	}
	
	public Mono<ServerResponse> getPostById(ServerRequest req){
		var postId = Long.valueOf(req.pathVariable("postid"));
		return this.postFacade.getPostById(postId)
				.flatMap(post -> ServerResponse.ok()
						.contentType(MediaType.APPLICATION_JSON)
						.bodyValue(post))
				.switchIfEmpty(ServerResponse.notFound().build());
	}
	
	public Mono<ServerResponse> updatePost(ServerRequest req){
		return req.multipartData()
				.flatMap(postFacade::updatePost)
				.then(ServerResponse.ok().build());
	}
	
	public Mono<ServerResponse>  deletePost(ServerRequest req){
		return req.bodyToMono(Post.class)
				.flatMap(postFacade::deletePost)
				.then(ServerResponse.ok().build());
	}
}
