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
	
	public final static String BASE_URL = "/phots/up/api/";
	
	@Bean
	public RouterFunction<ServerResponse> route(
			PostHandler postHandler,
			LikeHandler likeHandler,
			LoginHandler loginHandler,
			CommentHandler commentHandler,
			RegistrationHandler regHandler,
			UserInfoHandler usrInfoHandler,
			ConfirmReqHandler confirmHandler){
		
		return RouterFunctions.route()
				.GET(BASE_URL+"posts", postHandler::getAllPosts)
				.GET(BASE_URL+"posts/{postid}", postHandler::getPostById)
				.GET(BASE_URL+"recommend", postHandler::getRecommendations)
				.POST(BASE_URL+"posts", accept(MULTIPART_FORM_DATA), postHandler::savePost)
				.DELETE(BASE_URL+"posts/{postid}", postHandler::deletePost)
				.PUT(BASE_URL+"posts/{postid}", accept(MULTIPART_FORM_DATA), postHandler::updatePost)
				.add(loginRouting(loginHandler))
				.add(commentsRouting(commentHandler))
				.add(likeRouting(likeHandler))
				.add(registrationRouting(regHandler))
				.add(usrInfoRouting(usrInfoHandler))
				.add(confirmRouting(confirmHandler))
				.build();
	}
	
	public RouterFunction<ServerResponse> loginRouting(LoginHandler loginHandler){
		return RouterFunctions.route()
				.POST(BASE_URL+"login", accept(APPLICATION_JSON), loginHandler::handleLogin)
				.build();
	}
	
	public RouterFunction<ServerResponse> commentsRouting(CommentHandler commentHandler){
		return RouterFunctions.route()
				.POST(BASE_URL+"{postid}/comments", accept(APPLICATION_JSON), commentHandler::saveComment)
				.PUT(BASE_URL+"{postid}/comments", accept(APPLICATION_JSON), commentHandler::updateComment)
				.DELETE(BASE_URL+"{postid}/comments/{id}", commentHandler::deleteComment)
				.build();
	}
	
	public RouterFunction<ServerResponse> likeRouting(LikeHandler likeHandler){
		return RouterFunctions.route()
				.POST(BASE_URL+"{postid}/likes", accept(APPLICATION_JSON), likeHandler::handleLikeRequest)
				.build();
	}
	
	public RouterFunction<ServerResponse> registrationRouting(RegistrationHandler regHandler){
		return RouterFunctions.route()
				.POST(BASE_URL+"signup", accept(APPLICATION_JSON), regHandler::handleRegistration)
				.build();
	}
	
	public RouterFunction<ServerResponse> confirmRouting(ConfirmReqHandler confirmHandler){
		return RouterFunctions.route()
				.GET(BASE_URL+"confirm/{code}", confirmHandler::confirmReg)
				.build();
	}
	
	public RouterFunction<ServerResponse> usrInfoRouting(UserInfoHandler usrInfoHandler){
		return RouterFunctions.route()
				.GET(BASE_URL+"profile/{userId}", usrInfoHandler::getUserInfo)
				.POST(BASE_URL+"profile", accept(MULTIPART_FORM_DATA), usrInfoHandler::saveUserInfo)
				.build();
	}
}
