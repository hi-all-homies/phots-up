package random.name.photsapp.services.author;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SubscribeRequest {

    private AuthorDetails subscriber;

    @NotBlank
    @Length(min = 3, max = 16)
    private String subscription;
}