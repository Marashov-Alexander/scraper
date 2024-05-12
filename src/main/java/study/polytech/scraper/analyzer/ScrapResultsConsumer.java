package study.polytech.scraper.analyzer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import study.polytech.scraper.DecisionStatus;
import study.polytech.scraper.OutputResultsHandler;
import study.polytech.scraper.entity.TUrlEntity;
import study.polytech.scraper.entity.TUrlSourcesEntity;
import study.polytech.scraper.phash.PHashCalculatorService;
import study.polytech.scraper.queue.ConfigurableQueue;
import study.polytech.scraper.queue.QueueElementsConsumer;
import study.polytech.scraper.repository.UrlRepository;
import study.polytech.scraper.repository.UrlSourceRepository;

@Component
public class ScrapResultsConsumer extends QueueElementsConsumer<ScrapResult> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScrapResultsConsumer.class);

    private final PHashCalculatorService pHashCalculatorService;
    private final OutputResultsHandler outputResultsHandler;
    private final UrlRepository urlRepository;
    private final UrlSourceRepository urlSourceRepository;
    private final int distanceThreshold;

    public ScrapResultsConsumer(@NonNull ConfigurableQueue<ScrapResult> openedPagesQueue,
                                @NonNull OutputResultsHandler outputResultsHandler,
                                @NonNull PHashCalculatorService pHashCalculatorService,
                                @NonNull UrlRepository urlRepository,
                                @NonNull UrlSourceRepository urlSourceRepository,
                                @NonNull @Value("${feature.phash.distance.threshold}") int distanceThreshold) {
        super(openedPagesQueue);
        this.outputResultsHandler = outputResultsHandler;
        this.pHashCalculatorService = pHashCalculatorService;
        this.urlRepository = urlRepository;
        this.urlSourceRepository = urlSourceRepository;
        this.distanceThreshold = distanceThreshold;
    }

    @Override
    protected void process(@NonNull ScrapResult scrapResult) {

        TUrlEntity urlEntity = urlRepository.findById(scrapResult.getRequest().getUrlId()).orElse(null);
        if (urlEntity == null) {
            LOGGER.error("No saved entity for result [{}]", scrapResult);
            urlEntity = createNewEntity(scrapResult);
        }

        TUrlEntity updatedUrlEntity = updateUrlEntity(scrapResult, urlEntity);
        urlRepository.save(updatedUrlEntity);
        saveUrlSources(scrapResult.getRequest().getUrlId(), scrapResult);

        ModerationResult moderationResult = new ModerationResult(scrapResult.getRequest().getUrl(),
                scrapResult.getFinalUrl(), scrapResult.getTitle(),
                scrapResult.getDefaultScreenshotName(), scrapResult.getScreenshotWithoutMediaName(),
                updatedUrlEntity.getReferenceScreenshotHash(), updatedUrlEntity.getReferenceLightScreenshotHash());
        outputResultsHandler.putResults(moderationResult);
    }

    private void saveUrlSources(long id, @NonNull ScrapResult scrapResult) {
        String pageSource = scrapResult.getPageSource();
        String variables = scrapResult.getVariables();
        String finalUrl = scrapResult.getFinalUrl();
        String url = scrapResult.getRequest().getUrl();
        TUrlSourcesEntity urlSourcesEntity = new TUrlSourcesEntity();
        urlSourcesEntity.setFinalUrl(finalUrl);
        urlSourcesEntity.setCode(pageSource);
        urlSourcesEntity.setSourceUrl(url);
        urlSourcesEntity.setVariables(variables);
        urlSourcesEntity.setId(id);
        urlSourceRepository.save(urlSourcesEntity);
    }

    private TUrlEntity createNewEntity(ScrapResult scrapResult) {
        TUrlEntity entity = new TUrlEntity();
        entity.setId(scrapResult.getRequest().getUrlId());
        entity.setBaseUrl(scrapResult.getRequest().getUrl());
        entity.setDateCreated(System.currentTimeMillis());
        entity.setDateUpdated(System.currentTimeMillis());
        return entity;
    }

    @NonNull
    private TUrlEntity updateUrlEntity(@NonNull ScrapResult scrapResult, @NonNull TUrlEntity oldUrlEntity) {
        Long defaultScreenshotHash = pHashCalculatorService.calculateHash(scrapResult.getDefaultScreenshotName());
        Long screenshotWithoutMediaHash = pHashCalculatorService.calculateHash(scrapResult.getScreenshotWithoutMediaName());

        TUrlEntity newEntity = copyEntity(oldUrlEntity);

        long lastCheck = System.currentTimeMillis();
        boolean noReference = oldUrlEntity.getRefScreenshotName() == null;

        newEntity.setDateLastCheck(lastCheck);
        if (noReference) {
            // это новый reference
            setNewReference(scrapResult, defaultScreenshotHash, screenshotWithoutMediaHash, newEntity, lastCheck);
        } else {
            // сравниваем с reference. Сохраняем deviation
            setNewDeviation(scrapResult, defaultScreenshotHash, screenshotWithoutMediaHash, newEntity, lastCheck);

            int oldStatus = oldUrlEntity.getDecisionStatus();
            if (oldUrlEntity.getReferenceLightScreenshotHash() == null || screenshotWithoutMediaHash == null) {
                LOGGER.error("No light screenshot hash, unable to compare result [{}]", scrapResult);
                newEntity.setDecisionStatus(oldUrlEntity.getDecisionStatus());
            } else {
                int hammingDistance = Long.bitCount(oldUrlEntity.getReferenceLightScreenshotHash() ^ screenshotWithoutMediaHash);
                int newStatus;
                if (oldStatus == DecisionStatus.LIGHT_MODERATION.getCode() && hammingDistance > distanceThreshold) {
                    newStatus = DecisionStatus.DEFAULT_MODERATION.getCode();
                } else if (hammingDistance > distanceThreshold) {
                    newStatus = DecisionStatus.MANUAL_CHECK.getCode();
                } else {
                    newStatus = DecisionStatus.LIGHT_MODERATION.getCode();
                }
                newEntity.setLightScreenshotDistance(hammingDistance);
                newEntity.setDecisionStatus(newStatus);
            }
        }
        LOGGER.info("Old entity [{}] changed to new entity [{}] after result [{}]", oldUrlEntity, newEntity, scrapResult);
        return newEntity;
    }

    private static void setNewDeviation(ScrapResult scrapResult, Long defaultScreenshotHash, Long screenshotWithoutMediaHash, TUrlEntity newEntity, long lastCheck) {
        newEntity.setDateDeviationUpdated(lastCheck);
        newEntity.setDeviationScreenshotHash(defaultScreenshotHash);
        newEntity.setDeviationLightScreenshotHash(screenshotWithoutMediaHash);
        // TODO: удалить старые скриншоты, если имеются
        newEntity.setDevScreenshotName(scrapResult.getDefaultScreenshotName());
        newEntity.setDevLightScreenshotName(scrapResult.getScreenshotWithoutMediaName());
    }

    private static void setNewReference(ScrapResult scrapResult, Long defaultScreenshotHash, Long screenshotWithoutMediaHash, TUrlEntity newEntity, long lastCheck) {
        newEntity.setDateRefereceUpdated(lastCheck);
        newEntity.setReferenceScreenshotHash(defaultScreenshotHash);
        newEntity.setReferenceLightScreenshotHash(screenshotWithoutMediaHash);
        newEntity.setRefScreenshotName(scrapResult.getDefaultScreenshotName());
        newEntity.setRefLightScreenshotName(scrapResult.getScreenshotWithoutMediaName());
        newEntity.setDecisionStatus(DecisionStatus.LIGHT_MODERATION.getCode());
    }

    private static TUrlEntity copyEntity(TUrlEntity oldUrlEntity) {
        TUrlEntity newEntity = new TUrlEntity();
        newEntity.setId(oldUrlEntity.getId());
        newEntity.setBaseUrl(oldUrlEntity.getBaseUrl());
        newEntity.setUtmTemplate(oldUrlEntity.getUtmTemplate());
        newEntity.setDateUpdated(oldUrlEntity.getDateUpdated());
        newEntity.setDateCreated(oldUrlEntity.getDateCreated());
        newEntity.setDomainAgeInDays(oldUrlEntity.getDomainAgeInDays());
        newEntity.setDateRefereceUpdated(oldUrlEntity.getDateRefereceUpdated());
        newEntity.setReferenceScreenshotHash(oldUrlEntity.getReferenceScreenshotHash());
        newEntity.setReferenceLightScreenshotHash(oldUrlEntity.getReferenceLightScreenshotHash());
        newEntity.setRefScreenshotName(oldUrlEntity.getRefScreenshotName());
        newEntity.setRefLightScreenshotName(oldUrlEntity.getRefLightScreenshotName());
        newEntity.setDecisionStatus(oldUrlEntity.getDecisionStatus());
        newEntity.setDateDeviationUpdated(oldUrlEntity.getDateDeviationUpdated());
        newEntity.setDeviationScreenshotHash(oldUrlEntity.getDeviationScreenshotHash());
        newEntity.setDeviationLightScreenshotHash(oldUrlEntity.getDeviationLightScreenshotHash());
        newEntity.setDevScreenshotName(oldUrlEntity.getDevScreenshotName());
        newEntity.setDevLightScreenshotName(oldUrlEntity.getDevLightScreenshotName());
        newEntity.setDateLastCheck(oldUrlEntity.getDateLastCheck());
        newEntity.setLightScreenshotDistance(oldUrlEntity.getLightScreenshotDistance());
        return newEntity;
    }
}
