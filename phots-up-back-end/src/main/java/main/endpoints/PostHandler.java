package main.endpoints;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import lombok.RequiredArgsConstructor;
import main.facades.post.PostFacade;
import main.model.dto.PostSummary;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class PostHandler {
	private final PostFacade postFacade;

	
	public Mono<ServerResponse> getAllPosts(ServerRequest req){
		var page = req.queryParam("page").get();
		var token = req.cookies().getFirst("token").getValue();
		var fluxPosts = this.postFacade.getPosts(Integer.valueOf(page), token);
		return this.genereateResponseFromFlux(fluxPosts);
	}
	
	public Mono<ServerResponse> savePost(ServerRequest req){
		return req.multipartData()
				.flatMap(postFacade::storePost)
				.flatMap(post -> ServerResponse.status(HttpStatus.CREATED)
						.contentType(APPLICATION_JSON)
						.bodyValue(post));
	}
	
	public Mono<ServerResponse> getPostById(ServerRequest req){
		var postId = Long.valueOf(req.pathVariable("postid"));
		var token = req.cookies().getFirst("token").getValue();
		
		return this.postFacade.getPostById(postId, token)
				.flatMap(post -> ServerResponse.ok()
						.contentType(APPLICATION_JSON)
						.bodyValue(post))
				.switchIfEmpty(ServerResponse.notFound().build());
	}
	
	public Mono<ServerResponse> updatePost(ServerRequest req){
		return req.multipartData()
				.flatMap(postFacade::updatePost)
				.then(ServerResponse.ok().build());
	}
	
	public Mono<ServerResponse>  deletePost(ServerRequest req){
		var postId = Long.valueOf(req.pathVariable("postid"));
		return this.postFacade.deletePost(postId)
				.then(ServerResponse.ok().build());
	}
	
	public Mono<ServerResponse> getRecommendations(ServerRequest req){
		var token = req.cookies().getFirst("token").getValue();
		var fluxPosts = this.postFacade.getRecommendations(token);
		return this.genereateResponseFromFlux(fluxPosts);
	}
	
	private Mono<ServerResponse> genereateResponseFromFlux(Flux<PostSummary> posts){
		return posts.collectList()
				.flatMap(list -> ServerResponse.ok()
						.contentType(APPLICATION_JSON)
						.bodyValue(list));
	}
}
