package study.polytech.scraper.phash;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import study.polytech.scraper.scrap.ScreenshotManager;

import java.nio.file.Path;

@Component
public class PHashCalculatorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PHashCalculatorService.class);

    private final PHashCalculator pHashCalculator;
    private final ScreenshotManager screenshotManager;

    public PHashCalculatorService(@Value("${path.library.phash}") String libraryPath,
                                  @NonNull ScreenshotManager screenshotManager) {
        System.load(libraryPath);
        this.screenshotManager = screenshotManager;
        this.pHashCalculator = new PHashCalculator();
    }

    @Nullable
    public Long calculateHash(@Nullable String screenshotName) {
        if (screenshotName == null) {
            return null;
        }
        Path screenshotPath = screenshotManager.getScreenshotPath(screenshotName);
        long startTime = System.currentTimeMillis();
        try {
            // TODO: отправлять сразу два скриншота в библиотеку
            long hash = pHashCalculator.calculateHashes(screenshotPath + "|")[0];
            long deltaTime = System.currentTimeMillis() - startTime;
            LOGGER.info("For path [{}] got hash [{}] for [{}] ms", screenshotPath, hash, deltaTime);
            return hash;
        } catch (Throwable e) {
            long deltaTime = System.currentTimeMillis() - startTime;
            LOGGER.error("Hash calculation error for path [{}], took [{}] ms", screenshotName, deltaTime, e);
            return null;
        }
    }
}
