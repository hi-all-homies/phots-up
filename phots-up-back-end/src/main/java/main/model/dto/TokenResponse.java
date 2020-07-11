package main.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenResponse {
	private String token;
	
	public TokenResponse() {}

	public TokenResponse(String token) {
		this();
		this.token = token;
	}
}
