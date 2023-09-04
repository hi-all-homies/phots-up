package random.name.photsapp.services.post;

import random.name.photsapp.entities.Author;
import random.name.photsapp.entities.Comment;
import random.name.photsapp.entities.Post;
import java.util.List;

public interface PostService {

    Post findById(long id);

    List<Post> findAll(int page, int size);

    List<Post> findLiked(int page, int size, int likerId);

    Post save(AddPostRequest request);

    Comment addComment(ChangePostRequest request);

    boolean addLike(long postId, Author currentAuthor);

    void delete(int postId, Author currentAuthor);

    boolean update(ChangePostRequest request);
}