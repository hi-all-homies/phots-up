package random.name.photsapp.services.post;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import random.name.photsapp.services.author.AuthorDetails;


@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class ChangePostRequest {

    @NotBlank
    @Length(min = 3, max = 600)
    private String content;

    private long postId;

    private AuthorDetails currentUser;
}