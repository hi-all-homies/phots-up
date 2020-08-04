package main.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import main.dao.user.UserDao;
import main.dao.userinfo.UserInfoDao;
import main.model.entities.User;
import main.model.entities.UserInfo;

@SpringBootTest
public class UserInfoDaoTest {
	@Autowired UserDao userDao;
	@Autowired UserInfoDao userInfoDao;
	
	@Test
	void shoudWork() {
		var user = this.userDao.saveUser(new User(null, "hello", "world"));
		var info = new UserInfo(null,"hsbdsdsd.jpg", "hi....", user, null);
		
		this.userInfoDao.save(info);
		var res = this.userInfoDao.findUserInfoByUserId(user.getId());
		assertEquals("hello", res.getUser().getUsername());
	}
}
