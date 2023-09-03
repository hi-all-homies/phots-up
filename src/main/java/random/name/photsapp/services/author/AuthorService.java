package random.name.photsapp.services.author;

import random.name.photsapp.entities.Author;

public interface AuthorService {

    Author signUp(SignUpRequest request);

    Author findByUsername(String username);

    Author findFullByUsername(String username);

    void updatePassword(ChangeAuthorRequest request);

    void updateAvatar(ChangeAuthorRequest request);

    boolean subscribe(SubscribeRequest request);
}