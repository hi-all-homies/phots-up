package main.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import main.model.entities.User;
import main.services.user.UserService;
import reactor.test.StepVerifier;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
public class UserServiceImplTest {
	@Autowired
	UserService userService;
	
	@BeforeAll
	void voud() {
		var userToSave = new User(null, "12345", "password", "example@example.com");
		userService.registerUser(userToSave).subscribe();
	}
	
	@Test
	void shouldReturnUserDetails() {
		var result = userService.findByUsername("12345");
		
		StepVerifier.create(result)
			.assertNext(ud -> {
				assertEquals("12345", ud.getUsername());
				assertEquals(1, ud.getAuthorities().size());
				System.out.println(ud.getPassword());
			})
			.verifyComplete();
	}
	
	@Test
	void testingSave() {
		var userToSave = new User(null, "testUser", "password", "example@example.com");
		
		var result = userService.registerUser(userToSave);
		StepVerifier.create(result)
			.expectNext(true)
			.verifyComplete();
		
		var userToSave2 = new User(null, "12345", "password", "example@example.com");
		var result2 = userService.registerUser(userToSave2);
		StepVerifier.create(result2)
			.expectNext(false)
			.verifyComplete();
	}
	
}
