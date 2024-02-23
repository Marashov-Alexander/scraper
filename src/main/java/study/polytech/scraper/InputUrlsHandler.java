package study.polytech.scraper;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class InputUrlsHandler {

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
        if (analyzerResult.shouldProcess()) {
            urlsQueue.push(url);
        }
        return analyzerResult;
    }
}
