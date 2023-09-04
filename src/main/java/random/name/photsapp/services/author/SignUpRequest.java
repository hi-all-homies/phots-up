package random.name.photsapp.services.author;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SignUpRequest(
        @NotBlank @Length(min = 3, max = 16) String username,
        @NotBlank @Length(min = 8, max = 16) String password) {}