package main.dao.post;

import java.util.Collection;
import java.util.Optional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import main.dao.comment.CommentRepo;
import main.dao.user.UserRepo;
import main.model.entities.Post;

@Service
public class PostDaoImpl implements PostDao {
	private final PostRepo postRepo;
	private final UserRepo userRepo;
	private final CommentRepo commentRepo;
	
	public PostDaoImpl(PostRepo postRepo, UserRepo userRepo, CommentRepo commentRepo) {
		this.postRepo = postRepo;
		this.userRepo = userRepo;
		this.commentRepo = commentRepo;
	}

	@Override
	public Post savePost(Post post) {
		return this.postRepo.save(post);
	}
	
	@Override
	public Collection<Post> findAll(Pageable pageable) {
		var ids = this.postRepo.getPostIds(pageable);
		return this.postRepo.getPostsByIds(ids);
	}

	@Override
	public Optional<Post> findById(Long postId) {
		return this.postRepo.getById(postId);
	}

	@Override
	@Transactional
	public Optional<Post> deletePost(Long postId) {
		var postToDelete = this.postRepo.findById(postId);
		if (postToDelete.isPresent()) {
			this.commentRepo.deleteAllCommentsByPostId(postId);
			this.postRepo.deleteById(postId);
		}
		return postToDelete;
	}
	
	@Override
	@Transactional
	public long updatePost(Post post) {
		long id = post.getId();
		var storedPost = this.postRepo.findById(id).get();
		storedPost.setImageUrl(post.getImageUrl());
		storedPost.setContent(post.getContent());
		return id;
	}

	
	@Override
	public Collection<Post> findByUsername(String username){
		var pageable = PageRequest.of(0, 3, Sort.by(Direction.DESC, "post_id"));
		
		var ids = this.postRepo.getPostsByUsername(pageable, username);
		
		return this.postRepo.getPostsByIds(ids);
	}
	

	@Override
	@Transactional
	public boolean addLike(Long postId, Long userId) {
		var post = this.postRepo.getPostWithLikesById(postId);
		var user = this.userRepo.getOne(userId);
		
		 if (post.getLikes().contains(user)) {
			 post.getLikes().remove(user);
			 return false;
		 }
		 else {
			 post.getLikes().add(user);
			 return true;
		 }
	}
}
