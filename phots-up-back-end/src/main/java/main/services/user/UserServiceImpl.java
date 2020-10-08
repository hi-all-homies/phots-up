package main.services.user;

import java.util.UUID;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import main.dao.user.UserDao;
import main.model.entities.User;
import main.model.entities.UserInfo;
import main.model.entities.UserRole;
import main.services.emails.EmailsSender;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
public class UserServiceImpl implements UserService{
	private final UserDao userDao;
	private final PasswordEncoder encoder;
	private final EmailsSender emailsSender;
	
	public UserServiceImpl(UserDao userDao, PasswordEncoder encoder, EmailsSender emailsSender) {
		this.userDao = userDao;
		this.encoder = encoder;
		this.emailsSender = emailsSender;
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
		return Mono.defer(() -> Mono.justOrEmpty(this.userDao.isExisted(user)))
				.map(dbUser -> false)
				.switchIfEmpty(prepareAndSave(user));
	}
	
	private Mono<Boolean> prepareAndSave(User user){
		return Mono.just(user)
				.map(u -> {
						u.getRoles().add(UserRole.ROLE_USER);
						u.setPassword(this.encoder.encode(u.getPassword()));
						u.setConfirmCode(UUID.randomUUID().toString());
						return u; })
				.doOnNext(userDao::saveUser)
				.doOnNext(u -> emailsSender.sendEmail(u.getEmail(), u.getConfirmCode()))
				.map(u -> true);
	}
	
	
	@Override
	public Mono<User> getFullUser(Long userId) {
		return Mono.fromCallable(() -> {
			var user = this.userDao.loadById(userId);
			if (user.isEmpty())
				return null;
			else
				return user.get();
		});
	}

	@Override
	public Mono<User> setOrUpdateUserInfo(Long userId, UserInfo userInfo) {
		return Mono.fromCallable(() -> this.userDao.updateUserInfo(userId, userInfo));
	}
	
	@Override
	public boolean confirm(String code) {
		return this.userDao.confirm(code);
	}
}