package main.dao.user;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import main.model.entities.User;

@Repository
public interface UserRepo extends JpaRepository<User, Long>{
		
	@Query(value = "from User u left join fetch u.roles where username =:username")
	public Optional<User> findByUsername(@Param("username") String username);
	
	@Query(value = "from User u left join fetch u.roles left join fetch u.userInfo where u.id =:userId")
	public Optional<User> loadById(@Param("userId") Long userId);
	
	
	@Query(value = "select distinct u.username from users u left join posts p on u.user_id=p.user_id " +
			"left join post_likes lk on p.post_id=lk.post_id where lk.user_id =:currentUser " +
			"and p.user_id !=:currentUser",
			nativeQuery = true)
	public List<String> getLikedAuthorUsernames(@Param("currentUser") Long currentUser);
}
