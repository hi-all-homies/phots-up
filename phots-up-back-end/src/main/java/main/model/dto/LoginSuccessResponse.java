package main.model.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginSuccessResponse {
	private final long userId;
	private final String username;
	private final String avatarUrl;
}
