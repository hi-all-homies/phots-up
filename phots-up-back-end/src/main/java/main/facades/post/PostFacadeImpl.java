package main.facades.post;

import java.io.ByteArrayOutputStream;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.FormFieldPart;
import org.springframework.http.codec.multipart.Part;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import main.model.dto.PostSummary;
import main.model.entities.Post;
import main.security.TokenProvider;
import main.services.post.PostService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PostFacadeImpl implements PostFacade{
	private final PostService postService;
	private final TokenProvider tokenProvider;
	private final ObjectMapper mapper = new ObjectMapper();
	
	public PostFacadeImpl(PostService postService, TokenProvider tokenProvider) {
		this.postService = postService;
		this.tokenProvider = tokenProvider;
	}
	
	
	@Override
	public Mono<Post> storePost(MultiValueMap<String, Part> data) {
		var image = (FilePart) data.getFirst("image");
		var jsonPost = (FormFieldPart) data.getFirst("post");
		
		Post post = jsonToPost(jsonPost.value());
		
		return setImage(post, image)
				.flatMap(p -> postService.savePost(p));
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
		Post updatedPost = jsonToPost(jsonPost.value());
		
		var image = (FilePart) data.getFirst("image");
		
		return setImage(updatedPost, image)
				.flatMap(p -> postService.updatePost(p));
	}
	
	
	
	private Mono<Post> setImage(Post post, FilePart image) {
		if (image != null) {
			var outStream = new ByteArrayOutputStream();
			return image.content()
					.flatMap(db -> Flux.just(db.asByteBuffer().array()))
					.collectList()
					.map(list -> {
						list.forEach(bytes -> outStream.writeBytes(bytes));
						post.setImage(outStream.toByteArray());
						post.setImageKey(image.filename());
						return post;});
			}
		else return Mono.just(post);
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
