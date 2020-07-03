package main.dao.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.Collections;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import main.model.entities.User;
import main.model.entities.UserRole;

@SpringBootTest
public class UserDaoImplTest {
	@Autowired UserDao userDao;
	
	@Test
	void shouldSaveAndRetrieveUserJustFine() {
		Set<UserRole> roles = Collections.singleton(UserRole.ROLE_USER);
		var test_user = new User(null, "TEST_USER", "password", roles);
		
		var saved_user = userDao.saveUser(test_user);

		var retrieved_user = userDao.loadUserByUsername("TEST_USER").get();
		
		assertEquals(saved_user, retrieved_user);
	}
}
