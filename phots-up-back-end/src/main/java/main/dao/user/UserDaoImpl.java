package main.dao.user;

import java.util.List;
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

	@Override
	public List<String> getLikedAuthorUsernames(Long curentUserId) {
		return this.userRepo.getLikedAuthorUsernames(curentUserId);
	}

	@Override
	public Optional<User> loadById(Long userId) {
		return this.userRepo.loadById(userId);
	}
}
