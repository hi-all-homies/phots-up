package main.facades.comment;

import main.model.entities.Comment;
import reactor.core.publisher.Mono;

public interface CommentFacade {

	public Mono<Long> saveComment(Comment comment);
	
	public Mono<Integer> updateComment(Comment comment);
	
	public Mono<Void> deleteComment(Long commentId);
}
