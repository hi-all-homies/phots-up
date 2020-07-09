package main.services.like;

import org.springframework.stereotype.Service;
import main.dao.post.PostDao;
import main.model.dto.LikeRequest;
import reactor.core.publisher.Mono;

@Service
public class LikeServiceImpl implements LikeService{
	private final PostDao postDao;

	public LikeServiceImpl(PostDao postDao) {
		this.postDao = postDao;
	}

	@Override
	public Mono<Boolean> addLike(LikeRequest req) {
		return Mono.fromCallable(
				() -> postDao.addLike(req.getPost().getId(), req.getUser().getId()));
	}
}
