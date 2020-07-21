package main.model.notifications;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Builder;
import lombok.Getter;
import main.model.entities.Post;

@Getter
public class PostLikedNotification extends Notification {
	private final String whoLiked;
	private final long postId;
	private final String postContent;
	
	@Builder
	public PostLikedNotification(NotificationType type, String whoLiked, Post whichPost) {
		super(type, whichPost.getAuthor().getUsername());
		this.whoLiked = whoLiked;
		this.postId = whichPost.getId();
		this.postContent = whichPost.getContent();
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
