package study.polytech.scraper;

import com.google.common.collect.Lists;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.Validate;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.chromium.ChromiumDriverLogLevel;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v118.network.Network;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import study.polytech.scraper.resource.ProxyManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class Scraper {

    private static final Logger LOGGER = LoggerFactory.getLogger(Scraper.class);

    private final ProxyManager proxyManager;
    private final String screenshotDirectory;

    public Scraper(@NonNull ProxyManager proxyManager,
                   @Value("${path.screenshots.directory}") String screenshotDirectory) {
        Objects.requireNonNull(screenshotDirectory);
        this.proxyManager = proxyManager;
        this.screenshotDirectory = screenshotDirectory;
    }

    @NonNull
    public ScrapResult scrap(@NonNull String url) {
        LOGGER.info("Scrapping url [{}] started", url);

        ChromeDriver driver = openPage(url);
        if (driver == null) {
            return new ScrapResult(url, "scrap failed");
        }
        ScrapResult scrapResult = getScrapResult(url, driver);
        driver.quit();

        LOGGER.info("Scrapping url [{}] finished, result is [{}]", url, scrapResult);
        return scrapResult;
    }

    @Nullable
    private ChromeDriver openPage(@NonNull String url) {
        return proxyManager.doWithLocalProxy(localProxy -> {
            if (localProxy == null) {
                LOGGER.error("Unable to get proxy to process url [{}]", url);
                return null;
            }
            ChromeDriverService chromeDriverService = new ChromeDriverService.Builder()
                    .withLogOutput(System.out)
                    .withLogLevel(ChromiumDriverLogLevel.ALL)
                    .build();
            ChromeDriver driver = new ChromeDriver(chromeDriverService, createChromeOptions(localProxy));
            DevTools devTools = driver.getDevTools();
            devTools.createSession();
            devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
            List<String> blockedUrls = new ArrayList<>();
            blockedUrls.add("*://*/*.bmp");
            blockedUrls.add("*://*/*.gif");
            blockedUrls.add("*://*/*.png");
            blockedUrls.add("*://*/*.jpg");
            blockedUrls.add("*://*/*.jpeg");
            blockedUrls.add("*://*/*.webp");
            devTools.send(Network.setBlockedURLs(blockedUrls));
            try {
                driver.get(url);
                return driver;
            } catch (RuntimeException e) {
                LOGGER.error("Unable to process url [{}]", url, e);
                return null;
            }
        });
    }

    @NonNull
    private static ChromeOptions createChromeOptions(@NonNull String localProxy) {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setAcceptInsecureCerts(true);
//        chromeOptions.addArguments("--headless", "--disable-gpu");

        Proxy proxy = new Proxy()
                .setAutodetect(false)
                .setProxyType(Proxy.ProxyType.MANUAL)
                .setHttpProxy(localProxy)
                .setSslProxy(localProxy)
                .setFtpProxy(localProxy);

        chromeOptions
                .setProxy(proxy)
                .setPageLoadTimeout(Duration.ofSeconds(60))
                .addArguments("--disable-blink-features=AutomationControlled")
                .setExperimentalOption("excludeSwitches", Lists.newArrayList("enable-automation", "enable-logging"))
                .setExperimentalOption("useAutomationExtension", false);
        return chromeOptions;
    }

    @NonNull
    private ScrapResult getScrapResult(@NonNull String url, @NonNull ChromeDriver driver) {
        try {
            String title = driver.getTitle();
            Path screenshotPath = takeScreenshot(url, driver);
            return new ScrapResult(url, title, screenshotPath);
        } catch (RuntimeException e) {
            LOGGER.error("Failed to get scrap results for url [{}]", url, e);
            return new ScrapResult(url, "get results error");
        }
    }

    @Nullable
    private Path takeScreenshot(@NonNull String url, @NonNull ChromeDriver driver) {
        long startTime = System.currentTimeMillis();
        File tmpScreenshotFile = driver.getScreenshotAs(OutputType.FILE);
        int urlHash = Math.abs(url.hashCode());
        String screenshotPath = screenshotDirectory + urlHash + ".png";
        File screenshotFile = new File(screenshotPath);
        try {
            if (screenshotFile.exists()) {
                LOGGER.info("Old file [{}] removed [{}]", screenshotPath, screenshotFile.delete());
            }
            FileUtils.moveFile(tmpScreenshotFile, screenshotFile);
            long delta = startTime - System.currentTimeMillis();
            LOGGER.info("Got screenshot [{}] for url [{}] for [{}] ms", screenshotFile, url, delta);
            return screenshotFile.toPath();
        } catch (IOException e) {
            LOGGER.error("Error taking screenshot for url [{}]", url, e);
            return null;
        }
    }

}
