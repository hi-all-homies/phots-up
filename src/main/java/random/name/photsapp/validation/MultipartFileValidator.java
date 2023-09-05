package random.name.photsapp.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;
import java.util.Objects;
import java.util.Set;
import static org.springframework.http.MediaType.*;

public class MultipartFileValidator implements ConstraintValidator<MultipartFileConstraint, MultipartFile> {

    private Set<String> allowedTypes;

    @Override
    public void initialize(MultipartFileConstraint constraintAnnotation) {
        allowedTypes = Set.of(IMAGE_PNG_VALUE, IMAGE_JPEG_VALUE, IMAGE_GIF_VALUE);
    }

    @Override
    public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
        return Objects.nonNull(value) && Objects.nonNull(value.getContentType())
                && allowedTypes.contains(value.getContentType());
    }
}