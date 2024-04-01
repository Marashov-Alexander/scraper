package study.polytech.scraper.controller;

import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import study.polytech.scraper.ScrapResult;
import study.polytech.scraper.ScraperService;

@RestController("/")
public class ScraperRestController {

    private final ScraperService scraperService;

    public ScraperRestController(@NonNull ScraperService scraperService) {
        this.scraperService = scraperService;
    }

    @RequestMapping(path = "/scrap/sync", method = RequestMethod.GET)
    // TODO: change to POST with RequestBody param
    public ScrapResult scrapSync(@RequestParam String url) {
        return scraperService.scrapSync(url);
    }
}
