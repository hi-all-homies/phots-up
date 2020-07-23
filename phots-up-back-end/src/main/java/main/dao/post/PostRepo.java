package main.dao.post;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import main.model.entities.Post;

@Repository
public interface PostRepo extends JpaRepository<Post, Long>{
	
	@Query(value = "select p.id from Post p")
	public List<Long> getPostIds(Pageable pageable);
	
	@Query(value = "select distinct p from Post p left join fetch p.author left join fetch p.likes " +
										"left join fetch p.comments where p.id in (:ids)")
	public List<Post> getPostsByIds(@Param("ids") List<Long> ids);
	
	
	@Query(value = "from Post p left join fetch p.author left join fetch p.comments c " +
			"left join fetch c.author left join fetch p.likes where p.id =:id")
	public Optional<Post>getById(@Param("id") Long id);
	
	
	@Query(value = "from Post p left join fetch p.likes where p.id =:id")
	public Post getPostWithLikesById(@Param("id") Long id);

	@Modifying
	@Query(value = "update Post p set p.content =:content, p.imageKey =:imageKey where p.id =:id")
	public int updatePost(
			@Param("content") String content,
			@Param("imageKey") String imageKey,
			@Param("id") Long id);
	
	
	
	@Query(value = "select p.post_id from posts p left join users u on p.user_id = u.user_id " +
			"where u.username =:username",
			nativeQuery = true)
	public List<Long> getPostsByUsername(Pageable pageable, @Param("username") String username);
}
