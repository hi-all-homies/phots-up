package random.name.photsapp.services.post;

import random.name.photsapp.entities.Comment;
import random.name.photsapp.entities.Post;
import random.name.photsapp.services.author.AuthorDetails;
import java.util.List;
import java.util.Optional;

public interface PostService {

    Optional<Post> findById(long id);

    List<Post> findAll(int page, int size);

    List<Post> findLiked(int page, int size, long likerId);

    Post save(AddPostRequest request);

    Comment addComment(ChangePostRequest request);

    boolean addLike(long postId, AuthorDetails currentUser);

    void delete(long postId, AuthorDetails currentUser);

    boolean update(ChangePostRequest request);
}