package random.name.photsapp.services.post;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;
import random.name.photsapp.entities.Author;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddPostRequest {

    private MultipartFile image;

    @NotBlank
    @Length(min = 3, max = 600)
    private String content;

    private Author currentAuthor;
}