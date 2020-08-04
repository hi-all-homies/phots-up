package main.facades.user;

import org.springframework.http.codec.multipart.Part;
import org.springframework.util.MultiValueMap;
import main.model.entities.UserInfo;
import reactor.core.publisher.Mono;

public interface UserInfoFacade {
	
	public Mono<UserInfo> getUserInfoByUserId(Long userId);
	
	public Mono<UserInfo> setOrUpdateUserInfo(MultiValueMap<String, Part> data, String token);
}
