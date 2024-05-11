package study.polytech.scraper.controller;

import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import study.polytech.scraper.ScrapRequest;
import study.polytech.scraper.analyzer.ModerationResult;
import study.polytech.scraper.ScraperService;

@RestController("/")
public class ScraperRestController {

    private final ScraperService scraperService;

    public ScraperRestController(@NonNull ScraperService scraperService) {
        this.scraperService = scraperService;
    }

    @RequestMapping(path = "/scrap/sync", method = RequestMethod.GET)
    // TODO: change to POST with RequestBody param
    public ModerationResult scrapSync(@RequestParam(required = true) String url,
                                      @RequestParam(required = false) Boolean disableMedia,
                                      @RequestParam(required = false) Boolean emulateDevice,
                                      @RequestParam(required = false) Long urlId) {
        String myUrl = "https://sversheno-suditt.sbs/my-news/offerta.php?clid=cmVmPSZkcml2ZT0wJmM9bG9hZFRpbWVzKmNzaSZzPTg3MyoxOTEzKjc2NyozOTMqOTgwKjM5MyoyLjc1KjUmcD0maD04KjgqQWRyZW5vJTIwKFRNKSUyMDYxOCZ0PVNhdCUyME1heSUyMDExJTIwMjAyNCUyMDE3JTNBMzclM0E0NiUyMEdNVCUyQjAzMDAlMjAoJUQwJTlDJUQwJUJFJUQxJTgxJUQwJUJBJUQwJUIyJUQwJUIwJTJDJTIwJUQxJTgxJUQxJTgyJUQwJUIwJUQwJUJEJUQwJUI0JUQwJUIwJUQxJTgwJUQxJTgyJUQwJUJEJUQwJUJFJUQwJUI1JTIwJUQwJUIyJUQxJTgwJUQwJUI1JUQwJUJDJUQxJThGKSZxPQ==";
        return scraperService.scrapSync(myUrl, disableMedia, emulateDevice, urlId);
    }
}
