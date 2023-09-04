package random.name.photsapp.resource;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import random.name.photsapp.entities.Author;
import random.name.photsapp.services.author.AuthorService;
import random.name.photsapp.services.author.SignUpRequest;

@RestController
@RequestMapping("/signup")
public class SignUpEndpoint {

    private final AuthorService authorService;

    public SignUpEndpoint(AuthorService authorService) {
        this.authorService = authorService;
    }


    @PostMapping
    public ResponseEntity<Author> doSomething(@RequestBody @Valid SignUpRequest request){
        var author = this.authorService.signUp(request);

        return ResponseEntity.status(201)
                .body(author);
    }
}