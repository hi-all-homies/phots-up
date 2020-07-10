package main.services.user;

import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import main.model.entities.User;
import reactor.core.publisher.Mono;

public interface UserService extends ReactiveUserDetailsService{
	
	public Mono<Boolean> registerUser(User user);
}
