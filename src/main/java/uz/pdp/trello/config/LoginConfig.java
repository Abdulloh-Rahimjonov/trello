package uz.pdp.trello.config;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginConfig {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

}
