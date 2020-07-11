package main.facades.comment;

import org.springframework.stereotype.Service;
import main.model.entities.Comment;
import static main.model.notifications.NotificationType.POST_WAS_COMMENTED;
import main.model.notifications.PostCommentedNotificaton;
import main.services.comment.CommentService;
import main.services.notification.NotificationService;
import reactor.core.publisher.Mono;

@Service
public class CommentFacadeImpl implements CommentFacade {
	private final CommentService commentService;
	private final NotificationService notificationService;
	
	public CommentFacadeImpl(CommentService commentService, NotificationService notificationService) {
		this.commentService = commentService;
		this.notificationService = notificationService;
	}
	

	@Override
	public Mono<Long> saveComment(Comment comment) {
		return this.commentService.saveComment(comment)
				.doOnNext(savedComm ->{
					var notification = PostCommentedNotificaton.builder()
							.type(POST_WAS_COMMENTED)
							.comment(savedComm)
							.build();
					
					this.notificationService.sendNotification(notification);})
				.map(Comment::getId);
	}

	@Override
	public Mono<Integer> updateComment(Comment comment) {
		return this.commentService.updateComment(comment);
	}

	@Override
	public Mono<Void> deleteComment(Long commentId) {
		return this.commentService.deleteComment(commentId);
	}
}
