package random.name.photsapp.resource;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@RestControllerAdvice
public class ResponseExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value ={ ConstraintViolationException.class,
            IllegalStateException.class, UsernameNotFoundException.class })

    protected ResponseEntity<Object> handleListedExceptions(Exception ex, WebRequest request){
        String body = ex.getMessage();

        return handleExceptionInternal(ex, body, new HttpHeaders(), BAD_REQUEST, request);
    }
}