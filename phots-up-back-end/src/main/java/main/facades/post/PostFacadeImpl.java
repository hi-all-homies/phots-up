package main.facades.post;

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
import main.services.image.ImageService;
import main.services.post.PostService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PostFacadeImpl implements PostFacade{
	private final PostService postService;
	private final ImageService imageService;
	private final TokenProvider tokenProvider;
	private final ObjectMapper mapper = new ObjectMapper();
	
	public PostFacadeImpl(PostService postService, ImageService imageService, TokenProvider tokenProvider) {
		this.postService = postService;
		this.imageService = imageService;
		this.tokenProvider = tokenProvider;
	}
	
	
	@Override
	public Mono<Post> storePost(MultiValueMap<String, Part> data) {
		var key = this.imageService.storeImage((FilePart) data.getFirst("image"));
		var jsonPost = (FormFieldPart) data.getFirst("post");
		
		Post post = jsonToPost(jsonPost.value());
		post.setImageKey(key);
		return this.postService.savePost(post);
	}

	@Override
	public Flux<PostSummary> getPosts(int page, String token) {
		var currUserId = this.tokenProvider.getUserIdFromToken(token);
		return this.postService.getAllPosts(page, currUserId)
				.map(this::setImage);
	}
	
	@Override
	public Mono<PostSummary> getPostById(Long postId, String token) {
		var currUserId = this.tokenProvider.getUserIdFromToken(token);
		return this.postService.getPostById(postId, currUserId)
				.map(this::setImage);
	}
	
	@Override
	public Mono<Integer> updatePost(MultiValueMap<String,Part> data){
		var jsonPost = (FormFieldPart) data.getFirst("post");
		Post updatedPost = jsonToPost(jsonPost.value());
		
		var image = (FilePart) data.getFirst("image");
		if (image != null) {
			var imageKey = this.imageService.storeImage(image);
			var oldKey = updatedPost.getImageKey();
			updatedPost.setImageKey(imageKey);
			this.imageService.deleteImage(oldKey);
		}
		
		return this.postService.updatePost(updatedPost);
	}
	
	
	@Override
	public Mono<Void> deletePost(Long postId) {
		return this.postService.deletePost(postId)
				.doOnNext(deleted -> this.imageService.deleteImage(deleted.getImageKey()))
				.then();
	}

	
	
	@Override
	public Flux<PostSummary> getRecommendations(String token) {
		var currUserId = this.tokenProvider.getUserIdFromToken(token);
		
		return this.postService.getRecommendations(currUserId)
				.map(this::setImage);
	}


	private PostSummary setImage(PostSummary post) {
		var image = this.imageService.retrieveImageByKey(post.getPost().getImageKey());
		post.setImage(image);
		return post;
	}
	
	private Post jsonToPost(String jsonPost) {
		try {
			return this.mapper.readValue(jsonPost, Post.class);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e.getMessage());
		}
	}
}
