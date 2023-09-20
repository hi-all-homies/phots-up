package random.name.photsapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import random.name.photsapp.entities.Author;
import java.util.Optional;
import java.util.stream.Stream;

@Repository
public interface AuthorRepo extends JpaRepository<Author, Long> {

    Optional<Author> findByUsername(String username);

    @Modifying
    @Query("update Author a set a.password = :password where a.id = :id")
    int setPassword(@Param("password") String password, @Param("id") long id);

    @Modifying
    @Query("update Author a set a.avatarUrl = :avatar where a.id = :id")
    int setAvatarUrl(@Param("avatar") String avatarUrl, @Param("id") long id);


    @Query("""
            from Author a left join fetch a.subscriptions
            left join fetch a.subscribers
            where a.username = :username
            """)
    Author findFullByUsername(@Param("username") String username);


    Stream<Author> findByUsernameStartsWith(String username);
}