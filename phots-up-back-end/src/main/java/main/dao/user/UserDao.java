package main.dao.user;

import java.util.Optional;
import main.model.entities.User;

public interface UserDao {
	
	public User saveUser(User user);
	
	public Optional<User> loadUserByUsername(String username);
}
