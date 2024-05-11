package study.polytech.scraper.filter;

import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import study.polytech.scraper.repository.UrlRepository;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class WhoIsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(WhoIsService.class);
    private static final long INTERVAL_IN_SECONDS = 30;
    private static final String CREATION_DATE_STR = "creation date: ";
    private static final String CREATED_DATE_STR = "created:       ";

    private final UrlRepository repository;
    private final ScheduledExecutorService executorService;
    private Iterator<UrlInfo> urlInfos;

    public WhoIsService(@NonNull UrlRepository repository) {
        this.repository = repository;
        this.executorService = Executors.newSingleThreadScheduledExecutor();
        if (true) {
            return;
        }
        executorService.scheduleAtFixedRate(this::run, 0, INTERVAL_IN_SECONDS, TimeUnit.SECONDS);
    }

    @PreDestroy
    public void destroy() {
        executorService.shutdownNow();
    }

    private void run() {
        try {
            if (urlInfos == null || !urlInfos.hasNext()) {
                loadUrls();
            }
            if (!urlInfos.hasNext()) {
                LOGGER.info("No urls to process");
                return;
            }
            UrlInfo urlInfo = urlInfos.next();
            String domain = getDomainName(urlInfo.getBaseUrl());
            Long ageInDays = getDomainAgeInDays(domain);
            if (ageInDays == null) {
                return;
            }
            int ageInDaysInt = ageInDays.intValue();
            if (urlInfo.getAgeInDays() != null && urlInfo.getAgeInDays() == ageInDaysInt) {
                // значение не изменилось, сохранять в базу не требуется
                LOGGER.info("For url [{}] age in days value [{}] not changed", urlInfo, ageInDaysInt);
                return;
            }
            repository.updateDomainAgeDaysById(urlInfo.getId(), ageInDaysInt);
            LOGGER.info("For url [{}] saved age in days value [{}]", urlInfo, ageInDaysInt);
        } catch (Exception e) {
            LOGGER.error("Error when checking urls age in days", e);
        }
    }

    private void loadUrls() {
        List<UrlInfo> allUrls = repository.findAllUrls();
        urlInfos = allUrls.iterator();
        LOGGER.info("Loaded [{}] urls from repository", allUrls.size());
    }

    @Nullable
    private Long getDomainAgeInDays(@NonNull String domain) {
        try {
            Process process = Runtime.getRuntime().exec("whois " + domain);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                Long ageInDays = tryExtractDate(line, CREATION_DATE_STR);
                if (ageInDays == null) {
                    ageInDays = tryExtractDate(line, CREATED_DATE_STR);
                }
                if (ageInDays != null) {
                    return ageInDays;
                }
            }
            LOGGER.warn("No information about create date for domain [{}]", domain);
        } catch (Exception e) {
            LOGGER.error("Get creation date error for domain [{}]", domain, e);
        }
        return null;
    }

    @Nullable
    private static Long tryExtractDate(@NonNull String line, @NonNull String prefix) {
        int index = line.toLowerCase().indexOf(prefix);
        if (index >= 0) {
            String dateTimeStr = line.substring(index + prefix.length());
            Long ageInDays = convertZonedDateToAgeInDays(dateTimeStr);
            if (ageInDays == null) {
                ageInDays = convertDateToAgeInDays(dateTimeStr);
            }
            return ageInDays;
        }
        return null;
    }

    private static String getDomainName(@NonNull String url) throws URISyntaxException {
        URI uri = new URI(url);
        String domain = uri.getHost();
        return domain.startsWith("www.") ? domain.substring(4) : domain;
    }

    private static Long convertZonedDateToAgeInDays(String dateTimeString) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ");
            ZonedDateTime zonedDateTime = ZonedDateTime.parse(dateTimeString, formatter);
            LocalDate localDate = zonedDateTime.toLocalDate();
            return LocalDate.now().toEpochDay() - localDate.toEpochDay();
        } catch (RuntimeException e) {
            LOGGER.info("Unable to convert string [{}] to age in days as zoned date", dateTimeString);
            return null;
        }
    }

    private static Long convertDateToAgeInDays(String dateTimeString) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
            LocalDate localDate = LocalDate.parse(dateTimeString, formatter);
            return LocalDate.now().toEpochDay() - localDate.toEpochDay();
        } catch (RuntimeException e) {
            LOGGER.info("Unable to convert string [{}] to age in days as date", dateTimeString);
            return null;
        }
    }
}
