package main.dao;

import java.util.Collections;
import java.util.HashSet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import main.dao.comment.CommentDao;
import main.dao.post.PostDaoImpl;
import main.dao.user.UserDao;
import main.model.entities.Comment;
import main.model.entities.Post;
import main.model.entities.User;
import main.model.entities.UserRole;
import main.services.post.PostService;

@SpringBootTest
public class TestEM {
	@Autowired PostService service;
	@Autowired PostDaoImpl postDaoImpl;
	@Autowired UserDao userDao;
	@Autowired CommentDao commentDao;
	
	@BeforeEach
	void prepare() {
		var u  = new User(null,"user---1", "qwedsdsd", Collections.singleton(UserRole.ROLE_ADMIN));
		var user = userDao.saveUser(u);
		var u2  = new User(null,"user---1", "qwedsdsd", Collections.singleton(UserRole.ROLE_ADMIN));
		userDao.saveUser(u2);

		var p = new Post(null, "hello world", "frfdfd", user, new HashSet<User>(), new HashSet<Comment>());
		var post = postDaoImpl.savePost(p);
		postDaoImpl.addLike(1l, 1l);
		postDaoImpl.addLike(1l, 2l);
		var p2 = new Post(null, "test-post", "frfdfd", user, new HashSet<User>(), new HashSet<Comment>());
		postDaoImpl.savePost(p2);
		postDaoImpl.addLike(2l, 2l);
		
		var comment = new Comment(null, "hey you're a nice dancer", user, post);
		commentDao.saveComment(comment);
	}
	
	
	@Test
	void testGetAllMethod() {
		var psf = service.getAllPosts(0);
		psf.subscribe(ps -> {
			var str = String.format("%s %d, %d, author: %s", ps.getPost().getContent(), ps.getLikes(), ps.getComments(), ps.getPost().getAuthor().getUsername());
			System.out.println(str);
		});
	}
}
