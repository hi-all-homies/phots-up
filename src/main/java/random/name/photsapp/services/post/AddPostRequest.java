package random.name.photsapp.services.post;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;
import random.name.photsapp.services.author.AuthorDetails;
import random.name.photsapp.validation.MultipartFileConstraint;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddPostRequest {

    @MultipartFileConstraint
    private MultipartFile image;

    @NotBlank
    @Length(min = 3, max = 600)
    private String content;

    private AuthorDetails currentUser;
}