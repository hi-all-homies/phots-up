package main;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import main.dao.user.UserDao;
import main.model.entities.User;
import main.model.entities.UserRole;

@SpringBootApplication
public class Main {
	@Autowired UserDao userDao;
	
	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}
	
	@EventListener
	public void setUser(ApplicationReadyEvent event) {
		var user = new User(null, "anime-love" , "hello");
		user.setRoles(Collections.singleton(UserRole.ROLE_USER));
		
		this.userDao.saveUser(user);
	}

}
