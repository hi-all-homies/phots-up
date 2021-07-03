package main.services.like;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import main.dao.post.PostDao;
import main.model.dto.LikeRequest;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService{
	private final PostDao postDao;

	@Override
	public Mono<Boolean> addLike(LikeRequest req) {
		return Mono.fromCallable(
				() -> postDao.addLike(req.getPost().getId(), req.getUser().getId()))
				.subscribeOn(Schedulers.boundedElastic());
	}
}
