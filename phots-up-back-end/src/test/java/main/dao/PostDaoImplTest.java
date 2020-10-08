package main.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import main.dao.comment.CommentDao;
import main.dao.post.PostDaoImpl;
import main.dao.user.UserDao;
import main.model.entities.Comment;
import main.model.entities.Post;
import main.model.entities.User;
import main.model.entities.UserRole;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
public class PostDaoImplTest {
	@Autowired PostDaoImpl postDaoImpl;
	@Autowired UserDao userDao;
	@Autowired CommentDao commentDao;
	
	@BeforeAll
	void prepare() {
		var u  = new User(null,"user---1", "qwedsdsd", "example@example.com");
		u.getRoles().add(UserRole.ROLE_ADMIN);
		var user = userDao.saveUser(u);

		var p = new Post(null, "hello world", "frfdfd", user);
		var post = postDaoImpl.savePost(p);
		
		var p2 = new Post(null, "test-post", "frfdfd", user);
		postDaoImpl.savePost(p2);
		
		var comment = new Comment(null, "hey you're a nice dancer", user, post);
		commentDao.saveComment(comment);
	}
	
	@Test
	void shouldRetriveAllPosts() {
		System.out.println("---- getting all posts----");
		
		var posts = postDaoImpl.findAll(PageRequest.of(0, 7));
		posts.stream().forEach(p -> {
			var str = String.format("author: %s, img key: %s, comment count: %d, like count: %d",
					p.getAuthor().getUsername(), p.getImageKey(), p.getComments().size(), p.getLikes().size());
			System.out.println(str);
		});
	}
	
	@Test
	void shoudAddLikeToPost() {
		System.out.println("----------- Adding a like ---------");
		var result = postDaoImpl.addLike(2l, 1l);
		assertEquals(true, result);
	}
	
	@Test
	void shouldUpdatePost() {
		System.out.println("------ Updating ------");
		var p = new Post();
		p.setId(2l);
		p.setContent("new content");
		p.setImageKey("new key");
		
		this.postDaoImpl.updatePost(p);
	}
	
	@Test
	void shoudDeletePostWithItsLikesAndComments() {
		System.out.println("------- Deleting post-------");
		postDaoImpl.deletePost(1l);
	}
}
