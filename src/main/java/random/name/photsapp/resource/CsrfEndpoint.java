package random.name.photsapp.resource;

import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/csrf")
public class CsrfEndpoint {

    @GetMapping
    public ResponseEntity<CsrfToken> getToken(CsrfToken token){
        return ResponseEntity.ok(token);
    }
}
