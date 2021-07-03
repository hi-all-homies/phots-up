package main.services.user;

import java.util.UUID;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import main.dao.user.UserDao;
import main.model.entities.User;
import main.model.entities.UserInfo;
import main.model.entities.UserRole;
import main.services.emails.EmailsSender;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
	private final UserDao userDao;
	private final PasswordEncoder encoder;
	private final EmailsSender emailsSender;
	

	@Override
	public Mono<UserDetails> findByUsername(String username) {
		return Mono.defer(() -> Mono.justOrEmpty(
						this.userDao.loadUserByUsername(username)))
				.cast(UserDetails.class)
				.switchIfEmpty(Mono.error(new UsernameNotFoundException("there's no such username")))
				.subscribeOn(Schedulers.boundedElastic());
	}

	@Override
	public Mono<Boolean> registerUser(User user) {
		return Mono.defer(() -> Mono.justOrEmpty(this.userDao.isExisted(user)))
				.map(dbUser -> false)
				.switchIfEmpty(prepareAndSave(user))
				.subscribeOn(Schedulers.boundedElastic());
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
				return user.get();})
				.subscribeOn(Schedulers.boundedElastic());
	}

	@Override
	public Mono<User> setOrUpdateUserInfo(Long userId, UserInfo userInfo) {
		return Mono.fromCallable(() -> this.userDao.updateUserInfo(userId, userInfo))
				.subscribeOn(Schedulers.boundedElastic());
	}
	
	@Override
	public boolean confirm(String code) {
		return this.userDao.confirm(code);
	}
}