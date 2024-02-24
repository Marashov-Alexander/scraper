package study.polytech.scraper;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.chrome.ChromeDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.StandardCopyOption;

@Component
public class Scraper {

    private static final Logger LOGGER = LoggerFactory.getLogger(Scraper.class);

    @NonNull
    public ScrapResult scrap(@NonNull String url) {
        LOGGER.info("Scrapping url [{}] started", url);

        ChromeDriver driver = new ChromeDriver();
        driver.get(url);
        ScrapResult scrapResult = getScrapResult(url, driver);
        driver.quit();

        LOGGER.info("Scrapping url [{}] finished, result is [{}]", url, scrapResult);
        return scrapResult;
    }

    @NonNull
    private static ScrapResult getScrapResult(@NonNull String url, @NonNull ChromeDriver driver) {
        String title = driver.getTitle();
        String screenshotPath = takeScreenshot(url, driver);
        return new ScrapResult(url, title, screenshotPath);
    }

    @Nullable
    private static String takeScreenshot(@NonNull String url, @NonNull ChromeDriver driver) {
        File tmpScreenshotFile = driver.getScreenshotAs(OutputType.FILE);
        String screenshotPath = "./screenshots/" + Math.abs(url.hashCode()) + ".png";
        File screenshotFile = new File(screenshotPath);
        try {
            FileUtils.copyFile(tmpScreenshotFile, screenshotFile, StandardCopyOption.REPLACE_EXISTING);
            return screenshotPath;
        } catch (IOException e) {
            LOGGER.error("Error taking screenshot for url [{}]", url, e);
            return null;
        }
    }

}
