package study.polytech.scraper;

import com.google.common.collect.Lists;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.chromium.ChromiumDriverLogLevel;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v85.network.Network;
import org.openqa.selenium.devtools.v85.page.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import study.polytech.scraper.resource.ProxyManager;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class Scraper {

    private static final Logger LOGGER = LoggerFactory.getLogger(Scraper.class);

    private final ProxyManager proxyManager;

    public Scraper(@NonNull ProxyManager proxyManager) {
        this.proxyManager = proxyManager;
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
            devTools.send(Page.enable());
//            devTools.close();
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
    private static ScrapResult getScrapResult(@NonNull String url, @NonNull ChromeDriver driver) {
        try {
            String title = driver.getTitle();
            String screenshotPath = takeScreenshot(url, driver);
            return new ScrapResult(url, title, screenshotPath);
        } catch (RuntimeException e) {
            LOGGER.error("Failed to get scrap results for url [{}]", url, e);
            return new ScrapResult(url, "get results error");
        }
    }

    @Nullable
    private static String takeScreenshot(@NonNull String url, @NonNull ChromeDriver driver) {
        File tmpScreenshotFile = driver.getScreenshotAs(OutputType.FILE);
        String screenshotPath = "./screenshots/" + Math.abs(url.hashCode()) + ".png";
        File screenshotFile = new File(screenshotPath);
        try {
            FileUtils.moveFile(tmpScreenshotFile, screenshotFile);
            return screenshotPath;
        } catch (IOException e) {
            LOGGER.error("Error taking screenshot for url [{}]", url, e);
            return null;
        }
    }

}
