package study.polytech.scraper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import study.polytech.scraper.analyzer.ModerationResult;
import study.polytech.scraper.filter.UrlAnalyzerResult;
import study.polytech.scraper.profile.ProfileManager;
import study.polytech.scraper.profile.ScraperProfile;
import study.polytech.scraper.queue.QueueIsFullException;

import java.util.Objects;

@Service
public class ScraperService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScraperService.class);
    private static final long DEFAULT_RESULT_WAITING_TIMEOUT_MILLIS = 60_000L;

    private final InputUrlsHandler inputUrlsHandler;
    private final OutputResultsHandler outputResultsHandler;
    private final ProfileManager profileManager;

    public ScraperService(@NonNull InputUrlsHandler inputUrlsHandler,
                          @NonNull OutputResultsHandler outputResultsHandler,
                          @NonNull ProfileManager profileManager) {
        this.inputUrlsHandler = inputUrlsHandler;
        this.outputResultsHandler = outputResultsHandler;
        this.profileManager = profileManager;
    }

    public ModerationResult scrapSync(@NonNull String url,
                                      @Nullable Boolean disableMedia,
                                      @Nullable Boolean emulateDevice) {
        Objects.requireNonNull(url);
        ScraperProfile profile = profileManager.getProfile(url);
        ScrapRequest request = new ScrapRequest(url, disableMedia, emulateDevice, profile);
        LOGGER.info("ScrapSync invoked with request [{}] and profile [{}]", request, profile);
        try {
            UrlAnalyzerResult urlAnalyzerResult = inputUrlsHandler.handle(request);
            LOGGER.info("Url analyzer result is [{}] for request [{}]", urlAnalyzerResult, request);
            if (!urlAnalyzerResult.shouldProcess()) {
                return new ModerationResult(url, urlAnalyzerResult.name());
            }
            long start = System.currentTimeMillis();
            ModerationResult result = outputResultsHandler.waitForResults(url, DEFAULT_RESULT_WAITING_TIMEOUT_MILLIS);
            LOGGER.info("Waiting for results operation took [{}] ms for url [{}]", System.currentTimeMillis() - start, url);
            return result;
        } catch (QueueIsFullException e) {
            LOGGER.error("Queue is full, request [{}] rejected", request, e);
            return new ModerationResult(request.getUrl(), "Unable to process url. Queue is full");
        } catch (RuntimeException e) {
            LOGGER.error("An error occurred, url [{}] rejected", request, e);
            return new ModerationResult(request.getUrl(), "An error occurred" + e.getMessage());
        }
    }
}
