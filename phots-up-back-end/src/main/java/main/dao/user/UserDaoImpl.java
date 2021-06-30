package main.dao.user;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import main.model.entities.User;
import main.model.entities.UserInfo;

@Service
public class UserDaoImpl implements UserDao{
	private final UserRepo userRepo;
	private final UserInfoRepo userInfoRepo;
	
	public UserDaoImpl(UserRepo userRepo, UserInfoRepo userInfoRepo) {
		this.userRepo = userRepo;
		this.userInfoRepo = userInfoRepo;
	}
	
	@Override
	public User saveUser(User user) {
		return this.userRepo.save(user);
	}

	@Override
	public Optional<User> loadUserByUsername(String username) {
		return this.userRepo.findByUsername(username);
	}

	@Override
	public List<String> getLikedAuthorUsernames(Long curentUserId) {
		return this.userRepo.getLikedAuthorUsernames(curentUserId);
	}

	@Override
	public Optional<User> loadById(Long userId) {
		return this.userRepo.loadById(userId);
	}

	@Override
	@Transactional
	public User updateUserInfo(Long userId, UserInfo userInfo) {
		var user = this.userRepo.loadById(userId).get();
		
		if (user.getUserInfo() == null) {
			var newInfo = this.userInfoRepo.save(userInfo);
			user.setUserInfo(newInfo);
		}
		else {
			user.getUserInfo().setAboutMe(userInfo.getAboutMe());
			if (userInfo.getAvatar() != null) {
				user.getUserInfo().setAvatar(userInfo.getAvatar());
				user.getUserInfo().setAvatarKey(userInfo.getAvatarKey());
			}
		}
		return user;
	}

	@Override
	public Optional<User> isExisted(User user) {
		return this.userRepo.findByUsernameOrEmail(user.getUsername(), user.getEmail());
	}

	@Override
	@Transactional
	public boolean confirm(String code) {
		User user = this.userRepo.findByConfirmCode(code);
		if (user != null)
			user.setConfirmCode(null);
		return user != null;
	}
}
