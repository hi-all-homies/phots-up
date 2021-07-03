package main.facades.post;

import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.FormFieldPart;
import org.springframework.http.codec.multipart.Part;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import main.model.dto.PostSummary;
import main.model.entities.Post;
import main.security.TokenProvider;
import main.services.image.ImageService;
import main.services.post.PostService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PostFacadeImpl implements PostFacade{
	private final PostService postService;
	private final TokenProvider tokenProvider;
	private final ObjectMapper mapper = new ObjectMapper();
	private final ImageService imgService;
	
	
	@Override
	public Mono<Post> storePost(MultiValueMap<String, Part> data) {
		var image = (FilePart) data.getFirst("image");
		var jsonPost = (FormFieldPart) data.getFirst("post");
		
		final Post post = jsonToPost(jsonPost.value());
		
		return this.imgService.storeImage(image)
				.map(resp -> resp.getData())
				.doOnNext(d -> post.setImageUrl(d.getUrl()))
				.flatMap(d -> postService.savePost(post));
	}

	@Override
	public Flux<PostSummary> getPosts(int page, String token) {
		var currUserId = this.tokenProvider.getUserIdFromToken(token);
		return this.postService.getAllPosts(page, currUserId);
	}
	
	@Override
	public Mono<PostSummary> getPostById(Long postId, String token) {
		var currUserId = this.tokenProvider.getUserIdFromToken(token);
		return this.postService.getPostById(postId, currUserId);
	}
	
	@Override
	public Mono<Long> updatePost(MultiValueMap<String,Part> data){
		var jsonPost = (FormFieldPart) data.getFirst("post");
		final Post updatedPost = jsonToPost(jsonPost.value());
		
		var image = (FilePart) data.getFirst("image");
		
		return Mono.justOrEmpty(image)
				.flatMap(imgService::storeImage)
				.map(resp -> resp.getData())
				.flatMap(d -> {
					updatedPost.setImageUrl(d.getUrl());
					return postService.updatePost(updatedPost);})
				.switchIfEmpty(postService.updatePost(updatedPost));
	}
	
	
	
	@Override
	public Mono<Void> deletePost(Long postId) {
		return this.postService.deletePost(postId)
				.then();
	}

	
	
	@Override
	public Flux<PostSummary> getRecommendations(String token) {
		var currUserId = this.tokenProvider.getUserIdFromToken(token);
		
		return this.postService.getRecommendations(currUserId);
	}


	
	private Post jsonToPost(String jsonPost) {
		try {
			return this.mapper.readValue(jsonPost, Post.class);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e.getMessage());
		}
	}
}
