package study.polytech.scraper.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestPageController {

    @GetMapping("/testPage")
    public String testPage() {
        return "testPage"; // Имя HTML файла в src/main/resources/templates
    }

}
