package main.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import main.model.entities.Post;
import main.model.entities.User;

@Data
@NoArgsConstructor
public class LikeRequest {
	private User user;
	private Post post;
}
