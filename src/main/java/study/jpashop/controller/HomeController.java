package study.jpashop.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

//@Slf4j
@Controller
public class HomeController {

    Logger log = LoggerFactory.getLogger(getClass());

    @GetMapping("/")
    public String home() {
        log.info("home controller");
        return "home";
    }

}
