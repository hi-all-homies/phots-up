package main.facades.user;

import org.springframework.http.codec.multipart.Part;
import org.springframework.util.MultiValueMap;
import main.model.entities.User;
import reactor.core.publisher.Mono;

public interface UserInfoFacade {
	
	public Mono<User> getUserInfoByUserId(Long userId);
	
	public Mono<User> setOrUpdateUserInfo(MultiValueMap<String, Part> data, String token);
}
