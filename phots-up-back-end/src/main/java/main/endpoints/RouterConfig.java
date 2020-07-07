package main.endpoints;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import static org.springframework.http.MediaType.*;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class RouterConfig {
	
	@Bean
	public RouterFunction<ServerResponse> route(PostHandler postHandler){
		return RouterFunctions.route()
				.GET("/phots/up/api/posts", postHandler::getAllPosts)
				.GET("/phots/up/api/posts/{postid}", postHandler::getPostById)
				.POST("/phots/up/api/posts", accept(APPLICATION_JSON), postHandler::savePost)
				.build();
	}
}
