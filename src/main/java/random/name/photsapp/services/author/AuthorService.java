package random.name.photsapp.services.author;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;
import random.name.photsapp.entities.Author;
import java.util.List;
import java.util.Optional;

public interface AuthorService extends UserDetailsService {

    Author signUp(SignUpRequest request);

    Optional<Author> findByUsername(String username);

    Author findFullByUsername(String username);

    boolean updatePassword(ChangePasswordRequest request);

    Optional<String> updateAvatar(MultipartFile avatar, AuthorDetails currentUser);

    boolean subscribe(SubscribeRequest request);

    List<Author> findByUsernameLike(String username, AuthorDetails user);
}