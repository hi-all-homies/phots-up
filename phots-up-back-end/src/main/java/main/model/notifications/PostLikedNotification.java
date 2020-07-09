package main.model.notifications;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Builder;
import lombok.Getter;
import main.model.entities.Post;

@Getter
public class PostLikedNotification extends Notification {
	private final String whoLiked;
	private final Post whichPost;
	
	@Builder
	public PostLikedNotification(NotificationType type, String whoLiked, Post whichPost) {
		super(type);
		this.whoLiked = whoLiked;
		this.whichPost = whichPost;
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
