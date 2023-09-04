package random.name.photsapp.services.author;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import random.name.photsapp.entities.Author;
import random.name.photsapp.repositories.AuthorRepo;
import random.name.photsapp.services.image.ImageService;
import random.name.photsapp.services.notification.Notification;
import random.name.photsapp.services.notification.NotificationService;
import random.name.photsapp.services.notification.NotificationType;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class DefaultAuthorService implements AuthorService {

    private final AuthorRepo authorRepo;

    private final ImageService imageService;

    private final NotificationService notificationService;

    private final PasswordEncoder encoder;


    @Override
    public Author signUp(SignUpRequest request) {
        var result = this.findByUsername(request.username());

        if (result.isPresent())
            throw new IllegalStateException(String.format("%s already exist", request.username()));

        var author = new Author();
        author.setUsername(request.username());
        author.setPassword(this.encoder.encode(request.password()));
        return this.authorRepo.save(author);
    }


    @Override
    public Optional<Author> findByUsername(String username) {
        return this.authorRepo.findByUsername(username);
    }


    @Override
    public Author findFullByUsername(String username) {
        return this.authorRepo.findFullByUsername(username);
    }


    @Override
    public boolean updatePassword(ChangePasswordRequest request) {
        var username = request.getCurrentAuthor().getUsername();

        var existed = this.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        var result = this.encoder.matches(
                request.getOldPassword(), existed.getPassword());

        if (result){
            this.authorRepo.setPassword(
                    this.encoder.encode(request.getPassword()), existed.getId());
        }
        return result;
    }


    @Override
    public Optional<String> updateAvatar(MultipartFile avatar, Author currentAuthor) {

        return this.findByUsername(currentAuthor.getUsername())
                .map(author -> {
                    var avaUrl = this.imageService.upload(avatar);
                    this.authorRepo.setAvatarUrl(avaUrl, author.getId());
                    this.imageService.delete(author.getAvatarUrl());
                    return avaUrl;
                });
    }


    @Transactional
    @Override
    public boolean subscribe(SubscribeRequest request) {
        var subscriber = this.findFullByUsername(request.getSubscriber().getUsername());
        var subscription = this.findByUsername(request.getSubscription())
                .orElseThrow(() -> new UsernameNotFoundException(request.getSubscription()));

        boolean result;

        if (subscriber.getSubscriptions().contains(subscription)){
            result = !subscriber.getSubscriptions().remove(subscription);

            this.notificationService.sendNotification(new Notification(
                    NotificationType.USER_UNSUBSCRIBED, subscription, subscriber, "someone has unsubscribed."));
        }
        else {
            result = subscriber.getSubscriptions().add(subscription);

            this.notificationService.sendNotification(new Notification(
                    NotificationType.USER_SUBSCRIBED, subscription, subscriber, "new subscriber!"));
        }
        return result;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var author = this.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        return new AuthorDetails(author);
    }
}