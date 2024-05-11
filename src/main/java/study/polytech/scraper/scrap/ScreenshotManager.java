package study.polytech.scraper.scrap;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.chrome.ChromeDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import study.polytech.scraper.ScrapRequest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;

@Component
public class ScreenshotManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScreenshotManager.class);

    private static final String DEFAULT_OPERATION = "default";
    private static final String HIDE_MEDIA_OPERATION = "hidden";

    private final String screenshotDirectory;
    private final ScriptsManager scriptsManager;

    public ScreenshotManager(@Value("${path.screenshots.directory}") String screenshotDirectory,
                             @NonNull ScriptsManager scriptsManager) {
        Objects.requireNonNull(screenshotDirectory);
        Objects.requireNonNull(scriptsManager);
        this.screenshotDirectory = screenshotDirectory;
        this.scriptsManager = scriptsManager;
    }

    @NonNull
    public Path getScreenshotPath(@NonNull String screenshotName) {
        return Paths.get(screenshotDirectory + screenshotName + ".png");
    }

    @Nullable
    public String takeDefaultScreenshot(@NonNull ScrapRequest request, @NonNull ChromeDriver driver) {
        return request.isDisableMedia()
                ? null
                : takeScreenshot(request.getUrl(), driver, DEFAULT_OPERATION);
    }

    @Nullable
    public String takeScreenshotWithoutMedia(@NonNull ScrapRequest request, @NonNull ChromeDriver driver) {
        return scriptsManager.hideMedia(request, driver)
                ? takeScreenshot(request.getUrl(), driver, HIDE_MEDIA_OPERATION)
                : null;
    }

    @Nullable
    private String takeScreenshot(@NonNull String url, @NonNull ChromeDriver driver, @NonNull String operationName) {
        long startTime = System.currentTimeMillis();
        File tmpScreenshotFile = driver.getScreenshotAs(OutputType.FILE);
        String screenshotName = UUID.randomUUID().toString();
        // TODO: сохранять как уникальынй UUID, чтобы не перезаписывать предыдущие результаты
        Path screenshotPath = getScreenshotPath(screenshotName);
        File screenshotFile = screenshotPath.toFile();
        try {
            if (screenshotFile.exists()) {
                LOGGER.error("Old file [{}] removed [{}]", screenshotPath, screenshotFile.delete());
            }
            FileUtils.moveFile(tmpScreenshotFile, screenshotFile);
            long delta = System.currentTimeMillis() - startTime;
            LOGGER.info("Got [{}] screenshot [{}] for url [{}] for [{}] ms", operationName, screenshotFile, url, delta);
            return screenshotName;
        } catch (IOException e) {
            LOGGER.error("Error taking [{}] screenshot for url [{}]", operationName, url, e);
            return null;
        }
    }

}
