package random.name.photsapp.resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.GET;


@Controller
public class PathController {

    @RequestMapping( method = { GET }, path = { "/login", "/signup", "/profile/**", "/liked" } )
    public String forwardToSPA() {
        return "forward:/";
    }
}