package study.polytech.scraper;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class UrlsQueueConsumer extends QueueElementsConsumer<String> {

    private final ConfigurableQueue<ScrapResult> openedPagesQueue;
    private final Scraper scraper;

    public UrlsQueueConsumer(@NonNull ConfigurableQueue<String> urlsQueue,
                             @NonNull ConfigurableQueue<ScrapResult> openedPagesQueue,
                             @NonNull Scraper scraper) {
        super(urlsQueue);
        this.openedPagesQueue = openedPagesQueue;
        this.scraper = scraper;
    }

    @Override
    public void process(@NonNull String url) {
        // acquire the proxy and other resources?
        String result = scraper.scrap(url);
        // release the proxy
        try {
            openedPagesQueue.push(new ScrapResult(url, result));
        } catch (QueueIsFullException e) {
            logger.error("Unable to schedule results [{}] handling for url [{}]", result, url);
            throw new RuntimeException("Unable to schedule results handling", e);
        }
    }

}
