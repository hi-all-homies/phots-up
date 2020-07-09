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
import main.services.image.ImageService;
import main.services.post.PostService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PostFacadeImpl implements PostFacade{
	private final PostService postService;
	private final ImageService imageService;
	private final ObjectMapper mapper = new ObjectMapper();
	
	public PostFacadeImpl(PostService postService, ImageService imageService) {
		super();
		this.postService = postService;
		this.imageService = imageService;
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
	public Flux<PostSummary> getPosts(int page) {
		return this.postService.getAllPosts(page)
				.map(this::setImage);
	}
	
	@Override
	public Mono<PostSummary> getPostById(Long postId) {
		return this.postService.getPostById(postId)
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
	public Mono<Void> deletePost(Post post) {
		return this.postService.deletePost(post)
				.doOnSuccess(v -> this.imageService.deleteImage(post.getImageKey()));
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
