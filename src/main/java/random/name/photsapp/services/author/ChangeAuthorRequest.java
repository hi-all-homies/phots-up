package random.name.photsapp.services.author;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import random.name.photsapp.entities.Author;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class ChangeAuthorRequest {

    private MultipartFile avatar;

    private String password;

    private Author currentAuthor;
}