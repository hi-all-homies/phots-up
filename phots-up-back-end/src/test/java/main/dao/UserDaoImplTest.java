package main.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import main.dao.user.UserDaoImpl;
import main.model.entities.User;
import main.model.entities.UserRole;

@SpringBootTest
public class UserDaoImplTest {
	@Autowired UserDaoImpl userDao;
	
	@Test
	void shouldSaveAndRetrieveUserJustFine() {
		var test_user = new User(null, "TEST_USER", "password");
		test_user.getRoles().add(UserRole.ROLE_ADMIN);
		
		var saved_user = userDao.saveUser(test_user);

		var retrieved_user = userDao.loadUserByUsername("TEST_USER").get();
		
		assertEquals(saved_user, retrieved_user);
	}
}
