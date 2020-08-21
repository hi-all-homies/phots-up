package main.services.user;

import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import main.model.entities.User;
import main.model.entities.UserInfo;
import reactor.core.publisher.Mono;

public interface UserService extends ReactiveUserDetailsService{
	
	public Mono<Boolean> registerUser(User user);
	
	public Mono<User> getFullUser(Long userId);
	
	public Mono<User> setOrUpdateUserInfo(Long userId, UserInfo userInfo);
}
