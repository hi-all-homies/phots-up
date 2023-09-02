package random.name.photsapp.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import random.name.photsapp.entities.Post;
import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepo extends JpaRepository<Post, Long> {

    @Query("select p.id from Post p")
    List<Long> findPostIds(Pageable pageable);


    @Query(nativeQuery = true, value = """
            select post_id from post_likes
            where author_id = :authorId
            """)
    List<Long> findLikedPostIds(@Param("authorId") long authorId, Pageable pageable);


    @Query("""
            from Post p join fetch p.author
            left join fetch p.likes
            left join fetch p.comments
            where p.id in (:ids)
            """)
    List<Post> findPostsByIds(@Param("ids") List<Long> ids);


    @Query("""
            from Post p join fetch p.author
            left join fetch p.likes
            left join fetch p.comments
            where p.id = :id
            """)
    Optional<Post> findPostById(@Param("id") long id);
}