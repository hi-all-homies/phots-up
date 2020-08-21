package main.dao.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import main.model.entities.UserInfo;

@Repository
public interface UserInfoRepo extends JpaRepository<UserInfo, Long> {

}
