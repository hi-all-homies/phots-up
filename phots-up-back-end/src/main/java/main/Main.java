package main;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import main.model.entities.Post;
import main.model.entities.User;
import main.services.user.UserService;

@SpringBootApplication
public class Main {
	@Autowired UserService userService;
	
	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}
	
	
	@EventListener
	public void setUser(ApplicationReadyEvent event) throws IOException {
		var user = new User(null, "i-dont-know" , "password");
		this.userService.registerUser(user).subscribe();
		user.setId(1l);
		var post = new Post();
		post.setAuthor(user);
		post.setContent("hello world!!!");
		
		var bodyBuilder = new MultipartBodyBuilder();
		
		var res = new ClassPathResource("first-image.jpg");
		var image = res.getInputStream().readAllBytes();
		var jsonPost = new ObjectMapper().writeValueAsString(post);
		
		bodyBuilder.part("post", jsonPost, MediaType.TEXT_PLAIN)
			.header("Content-Disposition", "form-data; name=post")
			.header("Content-type", "text/plain");
		bodyBuilder.part("image", new ByteArrayResource(image))
			.header("Content-Disposition","form-data; name=image; filename=first-image.jpg");
		var body = bodyBuilder.build();
		
		WebClient client = WebClient.create("http://localhost:8080");

		client.post().uri("/phots/up/api/posts")
			.contentType(MediaType.MULTIPART_FORM_DATA)
			.body(BodyInserters.fromMultipartData(body))
			.exchange()
			.then().subscribe();
	}
}
