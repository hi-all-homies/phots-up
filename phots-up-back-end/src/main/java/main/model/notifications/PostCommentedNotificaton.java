package main.model.notifications;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Getter;
import main.model.entities.Comment;

@Getter
public class PostCommentedNotificaton extends Notification{
	private final Comment comment;

	@Builder
	public PostCommentedNotificaton(NotificationType type, Comment comment) {
		super(type, comment.getPost().getAuthor().getUsername());
		this.comment = comment;
	}

	@Override
	public String turnIntoJsonString() {
		try {
			var mapper = new ObjectMapper();
			return mapper.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e.getMessage());
		}
	}
}
