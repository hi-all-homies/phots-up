package main.model.notification;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import main.model.notifications.Notification;
import main.model.notifications.NotificationType;
import main.model.notifications.PostLikedNotification;

public class NotificationTest {

	@Test
	void shouldReturnStringRepresentationOfPostLikedNotification() {
		Notification notification =
				PostLikedNotification.builder()
					.type(NotificationType.POST_WAS_LIKED)
					.whoLiked("TEST_USER")
					.whichPost(1L)
					.build();
		
		var result = notification.turnIntoJsonString();
		assertEquals("{\"type\":\"POST_WAS_LIKED\",\"whoLiked\":\"TEST_USER\",\"whichPost\":1}", result);
	}
}
