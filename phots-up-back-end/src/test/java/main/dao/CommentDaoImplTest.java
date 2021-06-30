package main.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import main.dao.comment.CommentDaoImpl;
import main.dao.post.PostDao;
import main.dao.user.UserDao;
import main.model.entities.Comment;
import main.model.entities.Post;
import main.model.entities.User;
import main.model.entities.UserRole;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
 class CommentDaoImplTest {
	@Autowired UserDao userDao;
	@Autowired PostDao postDao;
	@Autowired CommentDaoImpl commentDao;
	
	@BeforeAll
	void prepare() {
		var u  = new User(null,"user---1", "qwedsdsd", "example@example.com");
		u.getRoles().add(UserRole.ROLE_ADMIN);
		var user = userDao.saveUser(u);

		var p = new Post(null, "hello world", user);
		var post = postDao.savePost(p);
		
		
		var comment = new Comment(null, "hey you're a nice dancer", user, post);
		commentDao.saveComment(comment);
	}
	
	@Test
	void shouldUpdateOneRow() {
		var comment = new Comment();
		comment.setId(1l);
		comment.setContent("new thing");
		var result = commentDao.updateComment(comment);
		assertEquals(1, result);
	}
	
}
