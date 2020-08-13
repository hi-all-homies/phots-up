package main.services.user;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import main.dao.user.UserDao;
import main.dao.userinfo.UserInfoDao;
import main.model.entities.User;
import main.model.entities.UserInfo;
import main.model.entities.UserRole;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
public class UserServiceImpl implements UserService{
	private final UserDao userDao;
	private final PasswordEncoder encoder;
	private final UserInfoDao userInfoDao;
	
	public UserServiceImpl(UserDao userDao, PasswordEncoder encoder, UserInfoDao userInfoDao) {
		this.userDao = userDao;
		this.encoder = encoder;
		this.userInfoDao = userInfoDao;
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
	
	
	@Override
	public Mono<UserInfo> getUserInfoByUserId(Long userId) {
		return Mono.fromCallable(
					() -> this.userInfoDao.findUserInfoByUserId(userId))
				.switchIfEmpty(gatherUserInfoIfEmpty(userId));
	}
	
	private Mono<UserInfo> gatherUserInfoIfEmpty(Long userId) {
		return Mono.justOrEmpty(
				this.userDao.loadById(userId)
					.map(user -> new UserInfo(null, null, null, user, null)));
	}

	@Override
	public Mono<UserInfo> setOrUpdateUserInfo(final Long userId, final UserInfo userInfo) {
		return Mono.fromCallable(
					() -> this.userInfoDao.findUserInfoByUserId(userId))
				.map(uInfo -> uInfo.update(userInfo))
				.switchIfEmpty(findUserAndSetToInfo(userId, userInfo))
				.doOnSuccess(userInfoDao::save);
	}
	
	private Mono<UserInfo>findUserAndSetToInfo(final Long userId, final UserInfo userInfo){
		return Mono.fromCallable(
				() -> this.userDao.loadById(userId))
				.map(optUser ->{
					var user = optUser.get();
					userInfo.setUser(user);
					return userInfo;
				});
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
