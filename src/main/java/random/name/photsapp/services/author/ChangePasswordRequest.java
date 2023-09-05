package random.name.photsapp.services.author;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;


@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class ChangePasswordRequest {

    @NotBlank @Length(min = 8, max = 16)
    private String password;

    @NotBlank @Length(min = 8, max = 16)
    private String oldPassword;

    private AuthorDetails currentUser;
}