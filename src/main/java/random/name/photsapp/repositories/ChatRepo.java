package random.name.photsapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import random.name.photsapp.entities.Author;
import random.name.photsapp.entities.Chat;
import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRepo extends JpaRepository<Chat, Long> {

    @Query("""
            from Chat c
            left join fetch c.sender
            left join fetch c.receiver
            where c.sender = :sender
            """)
    List<Chat> findBySender(@Param("sender") Author sender);

    Optional<Chat> findBySenderAndReceiver(Author sender, Author receiver);
}