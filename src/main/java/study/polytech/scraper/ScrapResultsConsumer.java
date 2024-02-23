package study.polytech.scraper;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class ScrapResultsConsumer extends QueueElementsConsumer<ScrapResult> {

    private final OutputResultsHandler outputResultsHandler;
    public ScrapResultsConsumer(@NonNull ConfigurableQueue<ScrapResult> openedPagesQueue,
                                @NonNull OutputResultsHandler outputResultsHandler) {
        super(openedPagesQueue);
        this.outputResultsHandler = outputResultsHandler;
    }

    @Override
    protected void process(@NonNull ScrapResult scrapResult) {
        String result = scrapResult.getResult();
        outputResultsHandler.putResults(scrapResult.getUrl(), result);
    }
}
