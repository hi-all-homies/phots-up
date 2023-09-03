package random.name.photsapp.services.post;

import random.name.photsapp.entities.Comment;
import random.name.photsapp.entities.Post;
import java.util.List;

public interface PostService {

    Post findById(long id);

    List<Post> findAll(int page, int size);

    List<Post> findLiked(int page, int size, int likerId);

    Post save(ChangePostRequest request);

    Comment addComment(ChangePostRequest request);

    boolean addLike(ChangePostRequest request);

    void delete(ChangePostRequest request);

    void update(ChangePostRequest request);
}