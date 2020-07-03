package main.model.notifications;

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
	
}
