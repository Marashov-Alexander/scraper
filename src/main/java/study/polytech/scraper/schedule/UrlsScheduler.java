package study.polytech.scraper.schedule;

import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import study.polytech.scraper.DecisionStatus;
import study.polytech.scraper.ScraperService;
import study.polytech.scraper.filter.UrlInfo;
import study.polytech.scraper.repository.UrlRepository;

import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class UrlsScheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(UrlsScheduler.class);
    private static final long MODERATION_INTERVAL_IN_SECONDS = 1 * 60;
    private static final int TRUSTED_DOMAIN_AGE_IN_DAYS = 100_000;

    private final UrlRepository urlRepository;
    private final ScraperService scraperService;
    private final ScheduledExecutorService moderationExecutor;
    private final ScheduledExecutorService scheduledUrlsExecutor;
    private final Random random;
    private final AtomicBoolean emulateDevice;

    public UrlsScheduler(@NonNull UrlRepository urlRepository,
                         @NonNull ScraperService scraperService) {
        this.urlRepository = urlRepository;
        this.scraperService = scraperService;
        this.moderationExecutor = Executors.newSingleThreadScheduledExecutor();
        this.scheduledUrlsExecutor = Executors.newSingleThreadScheduledExecutor();
        this.random = new Random();
        this.emulateDevice = new AtomicBoolean();

        this.moderationExecutor.scheduleAtFixedRate(this::scheduleModeration, 0L, MODERATION_INTERVAL_IN_SECONDS, TimeUnit.SECONDS);
    }

    @PreDestroy
    public void destroy() {
        this.moderationExecutor.shutdownNow();
        this.scheduledUrlsExecutor.shutdownNow();
    }

    private void scheduleModeration() {
        boolean emulateDeviceValue = emulateDevice.get();
        long minModerationTimeInMs = System.currentTimeMillis();
//        long minModerationTimeInMs = System.currentTimeMillis() - MODERATION_INTERVAL_IN_SECONDS;
        LOGGER.info("New moderation process started: emulateDevice=[{}], minModerationTime=[{}], trustedDomainAge=[{}]",
                emulateDeviceValue, minModerationTimeInMs, TRUSTED_DOMAIN_AGE_IN_DAYS);

        List<UrlInfo> urlsForModeration = urlRepository.findUrlsForModeration(TRUSTED_DOMAIN_AGE_IN_DAYS, minModerationTimeInMs);
        LOGGER.info("Found [{}] urls for moderation", urlsForModeration.size());

        for (UrlInfo urlInfo : urlsForModeration) {
            boolean disableMedia = urlInfo.getDecisionStatus() == DecisionStatus.LIGHT_MODERATION.getCode();
            long delayInSeconds = random.nextLong(MODERATION_INTERVAL_IN_SECONDS);
            scheduledUrlsExecutor.schedule(() -> putUrlToScraper(urlInfo, disableMedia, emulateDeviceValue), delayInSeconds, (TimeUnit) TimeUnit.SECONDS);
            LOGGER.info("Url [{}] scheduled with delay [{}] seconds", urlInfo, delayInSeconds);
        }

        LOGGER.info("Moderation process finished with device emulation parameter [{}]", emulateDeviceValue);
        emulateDevice.set(!emulateDeviceValue);
    }

    private void putUrlToScraper(@NonNull UrlInfo urlInfo, boolean disableMedia, boolean emulateDevice) {
        scraperService.scrapAsync(urlInfo.getBaseUrl(), disableMedia, emulateDevice, urlInfo.getId());
    }
}
