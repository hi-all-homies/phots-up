package main.services.user;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import main.dao.user.UserDao;
import main.model.entities.User;
import main.model.entities.UserRole;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
public class UserServiceImpl implements UserService{
	private final UserDao userDao;
	private final PasswordEncoder encoder;
	
	public UserServiceImpl(UserDao userDao, PasswordEncoder encoder) {
		this.userDao = userDao;
		this.encoder = encoder;
	}

	@Override
	public Mono<UserDetails> findByUsername(String username) {
		return Mono.defer(() -> Mono.justOrEmpty(
						this.userDao.loadUserByUsername(username)))
				.cast(UserDetails.class)
				.switchIfEmpty(Mono.error(new UsernameNotFoundException("there's no such username")))
				.subscribeOn(Schedulers.elastic());
	}

	@Override
	public Mono<Boolean> registerUser(User user) {
		return Mono.defer(() -> Mono.justOrEmpty(
						this.userDao.loadUserByUsername(user.getUsername())))
				.map(dbUser -> false)
				.switchIfEmpty(prepareAndSave(user));
	}
	
	private Mono<Boolean> prepareAndSave(User user){
		return Mono.just(user)
				.map(u -> {
						u.getRoles().add(UserRole.ROLE_USER);
						u.setPassword(this.encoder.encode(u.getPassword()));
						return u; })
				.doOnNext(userDao::saveUser)
				.map(u -> true);
	}
	
}
