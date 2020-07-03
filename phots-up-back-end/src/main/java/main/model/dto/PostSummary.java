package main.model.dto;

import lombok.Builder;
import lombok.Getter;
import main.model.entities.Post;

@Getter
@Builder
public class PostSummary {
	private final byte[] image;
	private final Post post;
	private final long likes;
	private final long comments;
}
