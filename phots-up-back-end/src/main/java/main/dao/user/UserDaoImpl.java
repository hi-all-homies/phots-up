package main.dao.user;

import java.util.Optional;
import org.springframework.stereotype.Service;
import main.model.entities.User;

@Service
public class UserDaoImpl implements UserDao{
	private final UserRepo userRepo;
	
	public UserDaoImpl(UserRepo userRepo) {
		this.userRepo = userRepo;
	}
	
	@Override
	public User saveUser(User user) {
		return this.userRepo.save(user);
	}

	@Override
	public Optional<User> loadUserByUsername(String username) {
		return this.userRepo.findByUsername(username);
	}
	
}
