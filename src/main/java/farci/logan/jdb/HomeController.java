package farci.logan.jdb;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping(path = "/")
    public String home() {
        System.out.println("Home page has been requested");
        return "index";
    }

}
