package main.dao.userinfo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import main.model.entities.UserInfo;

@Repository
public interface UserInfoRepo extends JpaRepository<UserInfo, Long>{
	
	@Query(value = "from UserInfo ui left join fetch ui.user u left join fetch u.roles r where u.id =:userId")
	public UserInfo findByUserId(@Param("userId") Long userId);
}
