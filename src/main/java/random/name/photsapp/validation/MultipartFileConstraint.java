package random.name.photsapp.validation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = MultipartFileValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface MultipartFileConstraint {

    String message() default "content type of the file must be: png, jpg, gif";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}