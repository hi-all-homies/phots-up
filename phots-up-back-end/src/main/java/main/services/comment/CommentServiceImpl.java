package main.services.comment;

import org.springframework.stereotype.Service;
import main.dao.comment.CommentDao;
import main.model.entities.Comment;
import reactor.core.publisher.Mono;

@Service
public class CommentServiceImpl implements CommentService {
	private final CommentDao commentDao;

	public CommentServiceImpl(CommentDao commentDao) {
		this.commentDao = commentDao;
	}

	@Override
	public Mono<Comment> saveComment(Comment comment) {
		return Mono.fromCallable(
					() -> this.commentDao.saveComment(comment));
	}

	@Override
	public Mono<Integer> updateComment(Comment comment) {
		return Mono.fromCallable(
				() -> this.commentDao.updateComment(comment));
	}

	@Override
	public Mono<Void> deleteComment(Long commentId) {
		return Mono.fromRunnable(
				() -> this.commentDao.deleteComment(commentId));
	}
}
