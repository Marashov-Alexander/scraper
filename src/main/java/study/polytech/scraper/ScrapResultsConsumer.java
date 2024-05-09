package study.polytech.scraper;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import study.polytech.scraper.phash.PHashCalculator;
import study.polytech.scraper.phash.PHashCalculatorService;

@Component
public class ScrapResultsConsumer extends QueueElementsConsumer<ScrapResult> {

    private final PHashCalculatorService pHashCalculatorService;
    private final OutputResultsHandler outputResultsHandler;

    public ScrapResultsConsumer(@NonNull ConfigurableQueue<ScrapResult> openedPagesQueue,
                                @NonNull OutputResultsHandler outputResultsHandler,
                                @NonNull PHashCalculatorService pHashCalculatorService) {
        super(openedPagesQueue);
        this.outputResultsHandler = outputResultsHandler;
        this.pHashCalculatorService = pHashCalculatorService;
    }

    @Override
    protected void process(@NonNull ScrapResult scrapResult) {
        Long pHash = calculateHash(scrapResult);
        ModerationResult moderationResult = new ModerationResult(scrapResult.getUrl(), scrapResult.getTitle(), scrapResult.getScreenshotPath(), pHash);
        outputResultsHandler.putResults(moderationResult);
    }

    @Nullable
    private Long calculateHash(@NonNull ScrapResult scrapResult) {
        return scrapResult.getScreenshotPath() == null ? null
                : pHashCalculatorService.calculateHash(scrapResult.getScreenshotPath());
    }
}
