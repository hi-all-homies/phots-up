package main.dao.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import main.model.entities.Comment;

@Repository
public interface CommentRepo extends JpaRepository<Comment, Long>{
	
	@Modifying
	@Query(value = "delete from comments c where c.post_id =:id", nativeQuery = true)
	public int deleteAllCommentsByPostId(@Param("id") Long id);

	@Modifying
	@Query(value = "update Comment c set c.content =:content where c.id =:id")
	public int updateById(@Param("content") String content, @Param("id") Long id);
}
