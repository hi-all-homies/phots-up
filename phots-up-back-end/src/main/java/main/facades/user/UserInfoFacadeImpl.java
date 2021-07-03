package main.facades.user;

import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.FormFieldPart;
import org.springframework.http.codec.multipart.Part;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import main.model.entities.User;
import main.model.entities.UserInfo;
import main.security.TokenProvider;
import main.services.image.ImageService;
import main.services.user.UserService;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserInfoFacadeImpl implements UserInfoFacade {
	private final UserService userService;
	private final TokenProvider tokenProvider;
	private final ImageService imgService;
	private final ObjectMapper mapper = new ObjectMapper();
	

	@Override
	public Mono<User> getUserInfoByUserId(Long userId) {
		return this.userService.getFullUser(userId);
	}

	@Override
	public Mono<User> setOrUpdateUserInfo(MultiValueMap<String, Part> data, String token) {
		final var userId = this.tokenProvider.getUserIdFromToken(token);
		
		var userInfoJson = (FormFieldPart) data.getFirst("userInfo");
		final var userInfo = jsonToUserInfo(userInfoJson.value());
		
		var avatar = (FilePart) data.getFirst("avatar");
		
		return Mono.justOrEmpty(avatar)
				.flatMap(imgService::storeImage)
				.map(resp -> resp.getData())
				.flatMap(d -> {
					userInfo.setAvatarUrl(d.getUrl());
					return userService.setOrUpdateUserInfo(userId, userInfo);})
				.switchIfEmpty(userService.setOrUpdateUserInfo(userId, userInfo));
	}
	
	

	private UserInfo jsonToUserInfo(String value) {
		try {
			return this.mapper.readValue(value, UserInfo.class);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e.getMessage());
		}
	}
}
