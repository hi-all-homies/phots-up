package random.name.photsapp.resource;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import random.name.photsapp.config.json.Views;
import random.name.photsapp.entities.Author;
import random.name.photsapp.services.author.AuthorDetails;
import random.name.photsapp.services.author.AuthorService;
import random.name.photsapp.services.author.ChangePasswordRequest;
import random.name.photsapp.services.author.SubscribeRequest;
import java.util.Objects;
import static random.name.photsapp.services.author.AuthorDetails.ROLE_USER;


@RestController
@RequestMapping("/api/author")
@RequiredArgsConstructor
public class AuthorEndpoint {

    private final AuthorService authorService;


    @JsonView(Views.BaseAuthor.class)
    @Secured(ROLE_USER)
    @GetMapping
    public ResponseEntity<Author> getAuthor(@AuthenticationPrincipal AuthorDetails user){
        return ResponseEntity.ok(user.getAuthor());
    }


    @JsonView(Views.FullAuthor.class)
    @GetMapping("{username}")
    public ResponseEntity<Author> findByUsername(@PathVariable("username") String username){
        var author = this.authorService.findFullByUsername(username);

        return Objects.nonNull(author) ? ResponseEntity.ok(author) :
                ResponseEntity.notFound().build();
    }


    @PatchMapping("/password")
    public ResponseEntity<Void> changePassword(
            @AuthenticationPrincipal AuthorDetails user,
            @RequestBody @Valid ChangePasswordRequest request){

        request.setCurrentAuthor(user.getAuthor());
        var result = this.authorService.updatePassword(request);

        return result ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }


    @PatchMapping(value = "/ava", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> changeAvatar(
            @AuthenticationPrincipal AuthorDetails user,
            @RequestPart MultipartFile avatar){

        return this.authorService.updateAvatar(avatar, user.getAuthor())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().body("error"));
    }


    @PostMapping
    public ResponseEntity<Boolean> subscribe(
            @AuthenticationPrincipal AuthorDetails user,
            @RequestBody @Valid SubscribeRequest request){

        request.setSubscriber(user.getAuthor());

        return ResponseEntity.ok(
                this.authorService.subscribe(request));
    }
}