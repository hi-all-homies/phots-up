package random.name.photsapp.services.post;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import random.name.photsapp.entities.Author;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class ChangePostRequest {

    private Author currentAuthor;

    private MultipartFile image;

    private String content;

    private long postId;
}