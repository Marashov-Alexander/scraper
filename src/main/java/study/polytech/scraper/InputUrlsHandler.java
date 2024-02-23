package study.polytech.scraper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class InputUrlsHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(InputUrlsHandler.class);

    private final UrlsFilter urlsFilter;
    private final ConfigurableQueue<String> urlsQueue;

    public InputUrlsHandler(@NonNull UrlsFilter urlsFilter,
                            @NonNull ConfigurableQueue<String> urlsQueue) {
        this.urlsFilter = urlsFilter;
        this.urlsQueue = urlsQueue;
    }

    @NonNull
    public UrlAnalyzerResult handle(@NonNull String url) throws QueueIsFullException {
        Objects.requireNonNull(url);
        UrlAnalyzerResult analyzerResult = urlsFilter.analyze(url);
        LOGGER.info("Url analyzer result is [{}] for url [{}]", analyzerResult, url);
        if (analyzerResult.shouldProcess()) {
            urlsQueue.push(url);
        }
        return analyzerResult;
    }
}
