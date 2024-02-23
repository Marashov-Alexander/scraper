package study.polytech.scraper.controller;

import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import study.polytech.scraper.InputUrlsHandler;
import study.polytech.scraper.OutputResultsHandler;
import study.polytech.scraper.QueueIsFullException;
import study.polytech.scraper.UrlAnalyzerResult;

@RestController("/")
public class ScraperRestController {

    private static final long DEFAULT_RESULT_WAITING_TIMEOUT_MILLIS = 10_000L;

    private final InputUrlsHandler inputUrlsHandler;
    private final OutputResultsHandler outputResultsHandler;

    public ScraperRestController(@NonNull InputUrlsHandler inputUrlsHandler,
                                 @NonNull OutputResultsHandler outputResultsHandler) {
        this.inputUrlsHandler = inputUrlsHandler;
        this.outputResultsHandler = outputResultsHandler;
    }

    @RequestMapping(path = "/scrap/sync", method = RequestMethod.GET)
    // TODO: change to POST with RequestBody param
    public String scrapSync(@RequestParam String url) {
        try {
            UrlAnalyzerResult urlAnalyzerResult = inputUrlsHandler.handle(url);
            if (urlAnalyzerResult.shouldProcess()) {
                return outputResultsHandler.waitForResults(url, DEFAULT_RESULT_WAITING_TIMEOUT_MILLIS);
            }
            return urlAnalyzerResult.name();
        } catch (QueueIsFullException e) {
            return "Unable to process url. Queue is full";
        }
    }
}
