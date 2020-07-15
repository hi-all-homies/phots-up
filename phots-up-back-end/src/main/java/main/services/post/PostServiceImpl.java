package main.services.post;

import java.util.Collection;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import main.dao.post.PostDao;
import main.model.dto.PostSummary;
import main.model.entities.Post;
import main.model.entities.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
public class PostServiceImpl implements PostService{
	public static final int PAGE_SIZE = 7;
	private final PostDao postDao;

	public PostServiceImpl(PostDao postDao) {
		this.postDao = postDao;
	}

	@Override
	public Flux<PostSummary> getAllPosts(int page, long currUserId) {
		var pageReq = PageRequest.of(page, PAGE_SIZE);
		
		return Flux.defer(() -> Flux.fromIterable(this.postDao.findAll(pageReq)))
				.subscribeOn(Schedulers.elastic())
				.map(post -> convert(post, currUserId));
	}

	@Override
	public Mono<PostSummary> getPostById(Long postId, long currUserId) {
		return Mono.defer(
						() -> Mono.justOrEmpty(this.postDao.findById(postId)))
				.subscribeOn(Schedulers.elastic())
				.map(post -> convertWhenFetchById(post, currUserId));
	}
	
	
	@Override
	public Mono<Post> savePost(Post post) {
		return Mono.defer(
						() -> Mono.just(this.postDao.savePost(post)))
				.subscribeOn(Schedulers.elastic());
	}

	@Override
	public Mono<Integer> updatePost(Post post) {
		return Mono.defer(
						() -> Mono.just(this.postDao.updatePost(post)))
				.subscribeOn(Schedulers.elastic());
	}

	@Override
	public Mono<Void> deletePost(Post post) {
		return Mono.fromRunnable(
				() -> this.postDao.deletePost(post.getId()));
	}

	private PostSummary convert(Post post, Long currentUserId) {
		var meLiked = this.getMeLiked(post.getLikes(), currentUserId);
		
		var postSummary = new PostSummary(
				post, post.getLikes().size(), post.getComments().size(), meLiked);
		post.setLikes(null);
		post.setComments(null);
		
		return postSummary;
	}
	
	private PostSummary convertWhenFetchById(Post post, Long currentUserId) {
		var meLiked = this.getMeLiked(post.getLikes(), currentUserId);
		
		return new PostSummary(
				post, post.getLikes().size(), post.getComments().size(), meLiked);
	}
	
	private boolean getMeLiked(Collection<User> likes, Long currentUserId) {
		return likes.parallelStream()
				.map(user -> user.getId())
				.anyMatch(id -> id.equals(currentUserId));
	}
	
}
