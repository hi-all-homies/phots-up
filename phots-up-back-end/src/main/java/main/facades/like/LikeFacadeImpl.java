package main.facades.like;

import org.springframework.stereotype.Service;
import main.model.dto.LikeRequest;
import main.model.notifications.NotificationType;
import main.model.notifications.PostLikedNotification;
import main.services.like.LikeService;
import main.services.notification.NotificationService;
import reactor.core.publisher.Mono;

@Service
public class LikeFacadeImpl implements LikeFacade{
	private final LikeService likeService;
	private final NotificationService notificationService;
	
	public LikeFacadeImpl(LikeService likeService, NotificationService notificationService) {
		this.likeService = likeService;
		this.notificationService = notificationService;
	}

	@Override
	public Mono<Boolean> addLike(LikeRequest req) {
		return this.likeService.addLike(req)
				.map(result -> {
					if (result) {
						var notification = PostLikedNotification.builder()
								.type(NotificationType.POST_WAS_LIKED)
								.whoLiked(req.getUser().getUsername())
								.whichPost(req.getPost())
								.build();
						this.notificationService.sendNotification(notification);
					}
					return result;
				});
	}
}
