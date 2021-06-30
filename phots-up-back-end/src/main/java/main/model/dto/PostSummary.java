package main.model.dto;

import lombok.Data;
import main.model.entities.Post;

@Data
public class PostSummary {
	private final Post post;
	private final int likes;
	private final int comments;
	private final boolean meLiked;
}
