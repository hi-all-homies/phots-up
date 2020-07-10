package main;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import main.model.entities.User;
import main.services.user.UserService;

@SpringBootApplication
public class Main {
	@Autowired UserService userService;
	
	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}
	
	@EventListener
	public void setUser(ApplicationReadyEvent event) {
		var user = new User(null, "anime-love" , "hello");
		
		this.userService.registerUser(user).subscribe();
	}

}
