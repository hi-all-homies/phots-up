package main.dao.user;

import java.util.List;
import java.util.Optional;
import main.model.entities.User;
import main.model.entities.UserInfo;

public interface UserDao {
	
	public User saveUser(User user);
	
	public Optional<User> loadUserByUsername(String username);
	
	public List<String> getLikedAuthorUsernames(Long curentUserId);
	
	public Optional<User> loadById(Long userId);
	
	public User updateUserInfo(Long userId, UserInfo userInfo);

	public Optional<User> isExisted(User user);
}
