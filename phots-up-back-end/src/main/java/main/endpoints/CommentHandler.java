package main.endpoints;

import static org.springframework.http.HttpStatus.CREATED;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import main.facades.comment.CommentFacade;
import main.model.entities.Comment;
import reactor.core.publisher.Mono;

@Component
public class CommentHandler {
	private final CommentFacade commentFacade;

	public CommentHandler(CommentFacade commentFacade) {
		this.commentFacade = commentFacade;
	}
	
	
	public Mono<ServerResponse> saveComment(ServerRequest req){
		return req.bodyToMono(Comment.class)
				.flatMap(commentFacade::saveComment)
				.flatMap(commId -> ServerResponse.status(CREATED)
						.bodyValue(commId));
	}
	
	public Mono<ServerResponse> updateComment(ServerRequest req){
		return req.bodyToMono(Comment.class)
				.flatMap(commentFacade::updateComment)
				.flatMap(rowsAffected -> ServerResponse.ok().build());
	}
	
	public Mono<ServerResponse> deleteComment(ServerRequest req){
		var commentId =Long.valueOf( req.pathVariable("id"));
		
		return this.commentFacade.deleteComment(commentId)
				.then(ServerResponse.ok().build());
	}
	
}
