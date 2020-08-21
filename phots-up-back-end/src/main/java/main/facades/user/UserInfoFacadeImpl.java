package main.facades.user;

import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.FormFieldPart;
import org.springframework.http.codec.multipart.Part;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import main.model.entities.User;
import main.model.entities.UserInfo;
import main.security.TokenProvider;
import main.services.image.ImageService;
import main.services.user.UserService;
import reactor.core.publisher.Mono;

@Service
public class UserInfoFacadeImpl implements UserInfoFacade {
	private final UserService userService;
	private final ImageService imageService;
	private final TokenProvider tokenProvider;
	private final ObjectMapper mapper = new ObjectMapper();
	private static final String AVATAR_FOLDER = "avatars";
	
	public UserInfoFacadeImpl(UserService userService, ImageService imageService, TokenProvider tokenProvider) {
		this.userService = userService;
		this.imageService = imageService;
		this.tokenProvider = tokenProvider;
	}

	@Override
	public Mono<User> getUserInfoByUserId(Long userId) {
		return this.userService.getFullUser(userId)
				.map(user -> {
					if (user.getUserInfo() != null && user.getUserInfo().getAvatarKey() != null) {
						byte[] avatar = this.imageService.retrieveImageByKey(
								user.getUserInfo().getAvatarKey(), AVATAR_FOLDER);
						user.getUserInfo().setAvatar(avatar);
					}
					return user;
				});
	}

	@Override
	public Mono<User> setOrUpdateUserInfo(MultiValueMap<String, Part> data, String token) {
		var userInfoJson = (FormFieldPart) data.getFirst("userInfo");
		var userInfo = jsonToUserInfo(userInfoJson.value());
		
		var avatar = (FilePart) data.getFirst("avatar");
		if (avatar != null) {
			var newKey = this.imageService.storeImage(avatar, AVATAR_FOLDER);
			var oldKey = userInfo.getAvatarKey();
			userInfo.setAvatarKey(newKey);
			if (oldKey != null)
				this.imageService.deleteImage(oldKey, AVATAR_FOLDER);
		}
		var userId = this.tokenProvider.getUserIdFromToken(token);
		return this.userService.setOrUpdateUserInfo(userId, userInfo);
	}

	private UserInfo jsonToUserInfo(String value) {
		try {
			return this.mapper.readValue(value, UserInfo.class);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e.getMessage());
		}
	}
}
