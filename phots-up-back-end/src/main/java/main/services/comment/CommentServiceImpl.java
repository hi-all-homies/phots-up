package main.services.comment;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import main.dao.comment.CommentDao;
import main.model.entities.Comment;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
	private final CommentDao commentDao;


	@Override
	public Mono<Comment> saveComment(Comment comment) {
		return Mono.fromCallable(
					() -> this.commentDao.saveComment(comment))
				.subscribeOn(Schedulers.boundedElastic());
	}

	@Override
	public Mono<Integer> updateComment(Comment comment) {
		return Mono.fromCallable(
					() -> this.commentDao.updateComment(comment))
				.subscribeOn(Schedulers.boundedElastic());
	}

	@Override
	public Mono<Void> deleteComment(Long commentId) {
		return Mono.fromRunnable(
					() -> this.commentDao.deleteComment(commentId));
	}
}
