package random.name.photsapp.resource;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import random.name.photsapp.config.json.Views;
import random.name.photsapp.entities.Comment;
import random.name.photsapp.entities.Post;
import random.name.photsapp.services.author.AuthorDetails;
import random.name.photsapp.services.post.AddPostRequest;
import random.name.photsapp.services.post.ChangePostRequest;
import random.name.photsapp.services.post.PostService;
import java.util.List;
import static random.name.photsapp.services.author.AuthorDetails.ROLE_USER;


@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostEndpoint {

    private final PostService postService;


    @JsonView(Views.PostView.class)
    @GetMapping("{id}")
    public ResponseEntity<Post> findById(@PathVariable("id") long id){
        return this.postService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @JsonView(Views.PostView.class)
    @GetMapping
    public ResponseEntity<List<Post>> findAll(@RequestParam int page, @RequestParam int size){
        return ResponseEntity.ok(this.postService.findAll(page, size));
    }


    @Secured(ROLE_USER)
    @JsonView(Views.PostView.class)
    @GetMapping("/liked")
    public ResponseEntity<List<Post>> findLiked(
            @AuthenticationPrincipal AuthorDetails user,
            @RequestParam int page, @RequestParam int size){

        return ResponseEntity.ok(this.postService.findLiked(page, size, user.getId()));
    }


    @JsonView(Views.PostView.class)
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Post> save(
            @AuthenticationPrincipal AuthorDetails user,
            @ModelAttribute @Valid AddPostRequest request){

        request.setCurrentUser(user);
        return ResponseEntity.ok(this.postService.save(request));
    }


    @PostMapping("/{id}/comment")
    public ResponseEntity<Comment> addComment(
            @PathVariable("id") long postId,
            @AuthenticationPrincipal AuthorDetails user,
            @RequestBody @Valid ChangePostRequest request){

        request.setPostId(postId);
        request.setCurrentUser(user);

        return ResponseEntity.ok(this.postService.addComment(request));
    }


    @PatchMapping("{id}/like")
    public ResponseEntity<Boolean> addLike(
            @PathVariable("id") long postId,
            @AuthenticationPrincipal AuthorDetails user){

        return ResponseEntity.ok(this.postService.addLike(postId, user));
    }


    @PutMapping("{id}")
    public ResponseEntity<Boolean> update(
            @PathVariable("id") long postId,
            @AuthenticationPrincipal AuthorDetails user,
            @RequestBody @Valid ChangePostRequest request){

        request.setPostId(postId);
        request.setCurrentUser(user);

        return ResponseEntity.ok(this.postService.update(request));
    }


    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(
            @PathVariable("id") long postId,
            @AuthenticationPrincipal AuthorDetails user){

        this.postService.delete(postId, user);

        return ResponseEntity.noContent().build();
    }
}