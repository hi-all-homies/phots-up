package main.services.post;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import main.dao.post.PostDao;
import main.model.dto.PostSummary;
import main.model.entities.User;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@Service
public class PostServiceImpl implements PostService{
	public static final int PAGE_SIZE = 7;
	private final PostDao postDao;

	public PostServiceImpl(PostDao postDao) {
		this.postDao = postDao;
	}

	@Override
	public Flux<PostSummary> getAllPosts(int page) {
		var pageReq = PageRequest.of(page, PAGE_SIZE);
		Long currUserId = 1l;
		
		return Flux.defer(() -> Flux.fromIterable(this.postDao.findAll(pageReq)))
				.subscribeOn(Schedulers.elastic())
				.map(post -> {
					var meLiked = post.getLikes().stream()
							.map(User::getId)
							.anyMatch(id -> id.equals(currUserId));
					
					return new PostSummary(post, post.getLikes().size(), post.getComments().size(), meLiked);
				});
	}
	
}
