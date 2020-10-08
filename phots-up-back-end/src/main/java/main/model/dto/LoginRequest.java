package main.model.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginRequest {
	private final String username;
	private final String password;
	private final String email;
	
	@JsonCreator
	public LoginRequest(
			@JsonProperty("username") String username,
			@JsonProperty("password") String password,
			@JsonProperty("email") String email) {
		this.username = username;
		this.password = password;
		this.email = email;
	}
}
