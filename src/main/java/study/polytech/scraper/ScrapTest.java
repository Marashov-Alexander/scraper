package study.polytech.scraper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class ScrapTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScrapTest.class);

    private static final Path INPUT_PATH = Paths.get("/Users/alexander.marashov/Documents/scraper-main/src/main/resources/urls.csv");
    private static final Path OUTPUT_PATH = Paths.get("/Users/alexander.marashov/Documents/scraper-main/src/main/resources/result_without_images.csv");
    private static final String RESULT_ROW_FORMAT = "%d±%s±%s±%s±%d\n";

    private final ScraperService scraperService;

    public ScrapTest(@NonNull ScraperService scraperService) {
        this.scraperService = scraperService;
    }

    public void scrapTestFile(int urlsOffsetIndex, int urlsLimit) {
        LOGGER.info("Scraping urls from [{}] to [{}] started", urlsOffsetIndex, urlsLimit);
        AtomicInteger counter = new AtomicInteger(urlsOffsetIndex);
        try (BufferedReader bufferedReader = Files.newBufferedReader(INPUT_PATH)) {
            try (BufferedWriter bufferedWriter = Files.newBufferedWriter(OUTPUT_PATH)) {
                bufferedReader.lines().skip(urlsOffsetIndex).limit(urlsLimit).forEach(url -> {
                    int index = counter.getAndIncrement();

                    long startTimeNanos = System.nanoTime();
                    ModerationResult scrapResult = scraperService.scrapSync(url);
                    long deltaNanos = System.nanoTime() - startTimeNanos;

                    saveResult(bufferedWriter, scrapResult, deltaNanos, index);
                });
            }
        } catch (Exception e) {
            LOGGER.error("Error processing urls, start index [{}], failure index [{}]", urlsOffsetIndex, counter.get(), e);
            return;
        }
        LOGGER.info("Scraping urls from [{}] to [{}] done", urlsOffsetIndex, urlsLimit);
    }

    private void saveResult(@NonNull BufferedWriter bufferedWriter, ModerationResult scrapResult, long deltaNanos, int index) {
        String record = String.format(RESULT_ROW_FORMAT, index, scrapResult.getUrl(), scrapResult.getScreenshotPath(), scrapResult.getErrorStatus(), deltaNanos);
        try {
            bufferedWriter.write(record);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}