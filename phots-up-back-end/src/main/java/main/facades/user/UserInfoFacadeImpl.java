package main.facades.user;

import java.io.ByteArrayOutputStream;
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
import main.services.user.UserService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserInfoFacadeImpl implements UserInfoFacade {
	private final UserService userService;
	private final TokenProvider tokenProvider;
	private final ObjectMapper mapper = new ObjectMapper();
	
	
	public UserInfoFacadeImpl(UserService userService, TokenProvider tokenProvider) {
		this.userService = userService;
		this.tokenProvider = tokenProvider;
	}

	@Override
	public Mono<User> getUserInfoByUserId(Long userId) {
		return this.userService.getFullUser(userId);
	}

	@Override
	public Mono<User> setOrUpdateUserInfo(MultiValueMap<String, Part> data, String token) {
		var userInfoJson = (FormFieldPart) data.getFirst("userInfo");
		var userInfo = jsonToUserInfo(userInfoJson.value());
		
		var avatar = (FilePart) data.getFirst("avatar");
		
		var userId = this.tokenProvider.getUserIdFromToken(token);
		
		return setAvatar(avatar, userInfo)
				.flatMap(uInf -> userService.setOrUpdateUserInfo(userId, uInf));
	}
	
	
	private Mono<UserInfo> setAvatar(FilePart avatar, UserInfo userInfo){
		if (avatar != null) {
			var outStream = new ByteArrayOutputStream();
			return avatar.content()
					.flatMap(db -> Flux.just(db.asByteBuffer().array()))
					.collectList()
					.map(list -> {
						list.forEach(bytes -> outStream.writeBytes(bytes));
						userInfo.setAvatar(outStream.toByteArray());
						userInfo.setAvatarKey(avatar.filename());
						return userInfo;});
			}
		else return Mono.just(userInfo);
	}
	

	private UserInfo jsonToUserInfo(String value) {
		try {
			return this.mapper.readValue(value, UserInfo.class);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e.getMessage());
		}
	}
}
