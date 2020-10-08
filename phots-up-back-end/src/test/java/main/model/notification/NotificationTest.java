package main.model.notification;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import main.model.entities.Post;
import main.model.entities.User;
import main.model.notifications.Notification;
import main.model.notifications.NotificationType;
import main.model.notifications.PostLikedNotification;

public class NotificationTest {

	@Test
	void shouldReturnStringRepresentationOfPostLikedNotification() {
		var user = new User(123l, "jimbo", "12345", "example@example.com");
		var post = new Post(null, "hello world...", null, user);
		
		Notification notification =
				PostLikedNotification.builder()
					.type(NotificationType.POST_WAS_LIKED)
					.whoLiked("TEST_USER")
					.whichPost(post)
					.build();
		
		var result = notification.turnIntoJsonString();
		
		assertEquals("{\"type\":\"POST_WAS_LIKED\",\"receiver\":\"jimbo\",\"whoLiked\":\"TEST_USER\",\"whichPost\":{\"id\":null,\"content\":\"hello world...\",\"imageKey\":null,\"author\":{\"id\":123,\"username\":\"jimbo\"},\"likes\":[],\"comments\":[]}}",
				result);
	}
}
