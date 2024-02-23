package study.polytech.scraper.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(ScraperRestController.class);
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
        LOGGER.info("ScrapSync invoked with url [{}]", url);
        try {
            UrlAnalyzerResult urlAnalyzerResult = inputUrlsHandler.handle(url);
            LOGGER.info("Url analyzer result is [{}] for url [{}]", urlAnalyzerResult, url);
            if (urlAnalyzerResult.shouldProcess()) {
                long start = System.currentTimeMillis();
                String s = outputResultsHandler.waitForResults(url, DEFAULT_RESULT_WAITING_TIMEOUT_MILLIS);
                LOGGER.info("Waiting for results operation took [{}] ms for url [{}]", System.currentTimeMillis() - start, url);
                return s;
            }
            return urlAnalyzerResult.name();
        } catch (QueueIsFullException e) {
            LOGGER.error("Queue is full, url [{}] rejected", url, e);
            return "Unable to process url. Queue is full";
        } catch (RuntimeException e) {
            LOGGER.error("An error occurred, url [{}] rejected", url, e);
            return "An error occurred" + e.getMessage();
        }
    }
}
