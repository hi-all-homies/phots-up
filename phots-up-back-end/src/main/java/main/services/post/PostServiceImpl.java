package main.services.post;

import java.util.Collection;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import main.dao.post.PostDao;
import main.dao.user.UserDao;
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
	private final UserDao userDao;

	public PostServiceImpl(PostDao postDao, UserDao userDao) {
		this.postDao = postDao;
		this.userDao = userDao;
	}

	@Override
	public Flux<PostSummary> getAllPosts(int page, long currUserId) {
		var pageReq = PageRequest.of(
				page, PAGE_SIZE, Sort.by(Direction.DESC, "id"));
		
		return Flux.defer(() -> Flux.fromIterable(this.postDao.findAll(pageReq)))
				.subscribeOn(Schedulers.elastic())
				.sort((x,y) -> y.getId().compareTo(x.getId()))
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
	public Mono<Long> updatePost(Post post) {
		return Mono.just(post)
				.map(p -> postDao.updatePost(p))
				.subscribeOn(Schedulers.elastic());
	}

	@Override
	public Mono<Post> deletePost(Long postId) {
		return Mono.justOrEmpty(this.postDao.deletePost(postId));
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
		
		var postSummary = new PostSummary(
				post, post.getLikes().size(), post.getComments().size(), meLiked);
		
		return postSummary;
	}
	
	private boolean getMeLiked(Collection<User> likes, Long currentUserId) {
		return likes.parallelStream()
				.map(user -> user.getId())
				.anyMatch(id -> id.equals(currentUserId));
	}
	

	@Override
	public Flux<PostSummary> getRecommendations(Long currentUserId) {
		return Flux.defer(
						() -> Flux.fromIterable(this.userDao.getLikedAuthorUsernames(currentUserId)))
				.flatMap(
						username -> Flux.fromIterable(this.postDao.findByUsername(username)))
				.map(post -> convert(post, currentUserId))
				.subscribeOn(Schedulers.elastic());
	}
}
