package main.services.comment;

import main.model.entities.Comment;
import reactor.core.publisher.Mono;

public interface CommentService {

	public Mono<Comment> saveComment(Comment comment);
	
	public Mono<Integer> updateComment(Comment comment);
	
	public Mono<Void> deleteComment(Long commentId);
}
