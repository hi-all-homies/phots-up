package main.dao.post;

import java.util.Collection;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import main.model.entities.Post;

public interface PostDao {
	
	public Post savePost(Post post);
	
	public Collection<Post> findAll(Pageable pageable);
	
	public Optional<Post> findById(Long postId);
	
	public Optional<Post> deletePost(Long postId);
	
	public long updatePost(Post post);
	
	public boolean addLike(Long postId, Long userId);
	
	public Collection<Post> findByUsername(String username);
	
}
