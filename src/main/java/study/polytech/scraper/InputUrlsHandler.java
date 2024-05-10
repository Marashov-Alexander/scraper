package study.polytech.scraper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import study.polytech.scraper.filter.UrlAnalyzerResult;
import study.polytech.scraper.filter.UrlsFilter;
import study.polytech.scraper.queue.ConfigurableQueue;
import study.polytech.scraper.queue.QueueIsFullException;

import java.util.Objects;

@Component
public class InputUrlsHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(InputUrlsHandler.class);

    private final UrlsFilter urlsFilter;
    private final ConfigurableQueue<ScrapRequest> urlsQueue;

    public InputUrlsHandler(@NonNull UrlsFilter urlsFilter,
                            @NonNull ConfigurableQueue<ScrapRequest> urlsQueue) {
        this.urlsFilter = urlsFilter;
        this.urlsQueue = urlsQueue;
    }

    @NonNull
    public UrlAnalyzerResult handle(@NonNull ScrapRequest request) throws QueueIsFullException {
        Objects.requireNonNull(request);
        UrlAnalyzerResult analyzerResult = urlsFilter.analyze(request.getUrl());
        LOGGER.info("Url analyzer result is [{}] for url [{}]", analyzerResult, request);
        if (analyzerResult.shouldProcess()) {
            urlsQueue.push(request);
        }
        return analyzerResult;
    }
}
