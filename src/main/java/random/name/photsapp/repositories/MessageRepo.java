package random.name.photsapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import random.name.photsapp.entities.Message;
import java.util.List;

@Repository
public interface MessageRepo extends JpaRepository<Message, Long> {

    List<Message> findByChatIdentity(String chatIdentity);
}