package study.polytech.scraper.scrap;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import study.polytech.scraper.ScrapRequest;
import study.polytech.scraper.analyzer.ScrapResult;
import study.polytech.scraper.queue.ConfigurableQueue;
import study.polytech.scraper.queue.QueueElementsConsumer;
import study.polytech.scraper.queue.QueueIsFullException;

@Component
public class UrlsQueueConsumer extends QueueElementsConsumer<ScrapRequest> {

    private final ConfigurableQueue<ScrapResult> openedPagesQueue;
    private final Scraper scraper;

    public UrlsQueueConsumer(@NonNull ConfigurableQueue<ScrapRequest> urlsQueue,
                             @NonNull ConfigurableQueue<ScrapResult> openedPagesQueue,
                             @NonNull Scraper scraper) {
        super(urlsQueue, 2);
        this.openedPagesQueue = openedPagesQueue;
        this.scraper = scraper;
    }

    @Override
    public void process(@NonNull ScrapRequest request) {
        ScrapResult result = scraper.scrap(request);
        try {
            openedPagesQueue.push(result);
        } catch (QueueIsFullException e) {
            logger.error("Unable to schedule results [{}] handling for request [{}]", result, request);
            throw new RuntimeException("Unable to schedule results handling", e);
        }
    }

}
