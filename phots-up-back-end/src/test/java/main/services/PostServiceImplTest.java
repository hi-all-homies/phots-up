package main.services;

import static java.util.Collections.singleton;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import main.dao.post.PostDao;
import main.model.entities.Comment;
import main.model.entities.Post;
import main.model.entities.User;
import main.model.entities.UserRole;
import main.services.post.PostServiceImpl;
import reactor.test.StepVerifier;

@SpringBootTest
public class PostServiceImplTest {
	@MockBean PostDao postDao;
	@Autowired PostServiceImpl postService;
	
	private Set<Post> posts;
	
	@BeforeEach
	public void setUp() {
		var role = singleton(UserRole.ROLE_USER);
		var u1 = new User(1l, "user---1", "pass");
		var u2 = new User(2l, "user---2", "pass222");
		u1.setRoles(role);
		u2.setRoles(role);
		
		var p1 = new Post(1l, "first post", "dfdffdfr4", u1);
		var p2 = new Post(2l, "hello world", "dffdferfdfr4", u1);
		var p3 = new Post(3l, "i'm here", "dffdfr45y5", u2);
		var likes = Set.of(u1, u2);
		p1.setLikes(likes);
		p2.setLikes(likes);
		p3.setLikes(likes);
		
		var c1 = new Comment(1l, "hi there", u1, p2);
		var c2 = new Comment(2l, "hi there", u2, p1);
		p2.getComments().add(c1);
		p1.getComments().add(c2);
		
		posts = Set.of(p1, p2, p3);
	}
	
	@Test
	void shoudReturnFluxOfPostSummary() {
		var page = PageRequest.of(0, 7);
		Mockito.when(postDao.findAll(page)).thenReturn(posts);
		
		var result = this.postService.getAllPosts(0);
		StepVerifier.create(result)
			.expectNextCount(3l)
			.verifyComplete();
	}
	@Test
	void shouldReturnMonoWIthPostSummaryIdOf1() {
		Mockito.when(postDao.findById(1l)).thenReturn(
				posts.stream()
					.filter(p -> p.getId().equals(1l))
					.findFirst());
		
		var result = this.postService.getPostById(1l);
		StepVerifier.create(result)
			.assertNext(ps -> {
				assertEquals(2, ps.getLikes());
				assertEquals("first post", ps.getPost().getContent());
			})
			.verifyComplete();
	}
}
