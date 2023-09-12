package random.name.photsapp.services.post;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import random.name.photsapp.entities.Comment;
import random.name.photsapp.entities.Post;
import random.name.photsapp.repositories.PostRepo;
import random.name.photsapp.services.author.AuthorDetails;
import random.name.photsapp.services.image.ImageService;
import random.name.photsapp.services.notification.Notification;
import random.name.photsapp.services.notification.NotificationService;
import random.name.photsapp.services.notification.NotificationType;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class DefaultPostService implements PostService {

    private final ImageService imageService;

    private final NotificationService notificationService;

    private final PostRepo postRepo;


    @Override
    public Optional<Post> findById(long id) {
        return this.postRepo.findPostById(id);
    }


    @Override
    public List<Post> findAll(int page, int size) {

        var ids = this.postRepo.findPostIds(this.createPageRequest(page, size, "id"));

        var posts = this.postRepo.findPostsByIds(ids);
        return sortPostsDesc(posts);
    }

    @Override
    public List<Post> findLiked(int page, int size, long likerId) {

        var ids = this.postRepo.findLikedPostIds(likerId, this.createPageRequest(page, size, "post_id"));

        var posts = this.postRepo.findPostsByIds(ids);
        return sortPostsDesc(posts);
    }

    @Override
    public Post save(AddPostRequest request) {
        var imgUrl = this.imageService.upload(request.getImage());

        var newPost = new Post();
        newPost.setAuthor(request.getCurrentUser().getAuthor());
        newPost.setContent(request.getContent());
        newPost.setImageUrl(imgUrl);
        return this.postRepo.save(newPost);
    }

    @Transactional
    @Override
    public Comment addComment(ChangePostRequest request) {
        var newComment = new Comment();
        newComment.setContent(request.getContent());
        newComment.setAuthor(request.getCurrentUser().getUsername());

        var post = this.postRepo.findPostById(request.getPostId())
                .orElseThrow(this::createNotFound);

        post.getComments().add(newComment);

        return newComment;
    }

    @Transactional
    @Override
    public boolean addLike(long postId, AuthorDetails currentUser) {
        var post = this.postRepo.findPostById(postId)
                .orElseThrow(this::createNotFound);

        boolean result;
        var currAuthor = currentUser.getAuthor();

        if (post.getLikes().contains(currAuthor)){
            result = !post.getLikes().remove(currAuthor);
        }
        else {
            result = post.getLikes().add(currAuthor);

            this.notificationService.sendNotification(new Notification(
                    NotificationType.POST_LIKED, post.getAuthor(), currAuthor,
                    String.format("%s has liked one of your posts!", currAuthor.getUsername())));
        }
        return result;
    }


    @Transactional
    @Override
    public void delete(long postId, AuthorDetails currentUser) {
        var post = this.postRepo.findByIdAndAuthor(postId, currentUser.getAuthor())
                .orElseThrow(this::createNotFound);

        this.postRepo.delete(post);
    }


    @Transactional
    @Override
    public boolean update(ChangePostRequest request) {
        return this.postRepo.findByIdAndAuthor(request.getPostId(), request.getCurrentUser().getAuthor())
                .map(post -> {
                    post.setContent(request.getContent());
                    return true;})
                .orElse(false);
    }


    private PageRequest createPageRequest(int page, int size, String sortBy){
        return PageRequest.of(page, size, Sort.Direction.DESC, sortBy);
    }

    private List<Post> sortPostsDesc(List<Post> posts){
        return posts.stream()
                .sorted((Comparator.comparingLong(Post::getId)
                        .reversed()))
                .toList();
    }

    private IllegalStateException createNotFound() {
        return new IllegalStateException("post not found");
    }
}