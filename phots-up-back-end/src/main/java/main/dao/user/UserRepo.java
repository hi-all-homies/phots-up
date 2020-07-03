package main.dao.user;

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
	
}
