package random.name.photsapp.services.author;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import random.name.photsapp.entities.Author;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SubscribeRequest {

    private Author subscriber;

    @NotBlank
    @Length(min = 3, max = 16)
    private String subscription;
}