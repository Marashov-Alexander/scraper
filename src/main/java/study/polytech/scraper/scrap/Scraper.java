package study.polytech.scraper.scrap;

import com.google.common.collect.Lists;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.chromium.ChromiumDriverLogLevel;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v118.emulation.Emulation;
import org.openqa.selenium.devtools.v118.emulation.model.UserAgentMetadata;
import org.openqa.selenium.devtools.v118.network.Network;
import org.openqa.selenium.devtools.v118.page.model.Viewport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import study.polytech.scraper.ScrapRequest;
import study.polytech.scraper.analyzer.ScrapResult;
import study.polytech.scraper.profile.ScraperProfile;
import study.polytech.scraper.proxy.ProxyManager;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class Scraper {

    private static final Logger LOGGER = LoggerFactory.getLogger(Scraper.class);

    private final ProxyManager proxyManager;
    private final ScreenshotManager screenshotManager;
    private final ScriptsManager scriptsManager;

    public Scraper(@NonNull ProxyManager proxyManager,
                   @NonNull ScreenshotManager screenshotManager,
                   @NonNull ScriptsManager scriptsManager) {
        this.proxyManager = proxyManager;
        this.screenshotManager = screenshotManager;
        this.scriptsManager = scriptsManager;
    }

    @NonNull
    public ScrapResult scrap(@NonNull ScrapRequest request) {
        LOGGER.info("Scrapping request [{}] started", request);

        ChromeDriver driver = openPage(request);
        if (driver == null) {
            return new ScrapResult(request, "scrap failed");
        }
        ScrapResult scrapResult = getScrapResult(request, driver);
        driver.quit();

        LOGGER.info("Scrapping request [{}] finished, result is [{}]", request, scrapResult);
        return scrapResult;
    }

    @Nullable
    private ChromeDriver openPage(@NonNull ScrapRequest request) {
        return proxyManager.doWithLocalProxy(localProxy -> {
            if (localProxy == null) {
                LOGGER.error("Unable to get proxy to process request [{}]", request);
                return null;
            }
            ChromeDriverService chromeDriverService = new ChromeDriverService.Builder()
                    .withLogOutput(System.out)
                    .withLogLevel(ChromiumDriverLogLevel.ALL)
                    .build();
            ChromeDriver driver = new ChromeDriver(chromeDriverService, createChromeOptions(localProxy));
            try {
                configureBrowser(driver, request);
                driver.get(request.getUrl());
                return driver;
            } catch (RuntimeException e) {
                LOGGER.error("Unable to process request [{}]", request, e);
                return null;
            }
        });
    }

    private void configureBrowser(ChromeDriver driver,
                                  ScrapRequest request) {
        DevTools devTools = driver.getDevTools();
        devTools.createSession();
        devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
        if (request.isDisableMedia()) {
            disableMedia(devTools);
        }
        emulateDevice(devTools, request.getProfile());
        scriptsManager.spoofJsScope(request, driver);
    }

    private static void emulateDevice(DevTools devTools, ScraperProfile profile) {
        devTools.send(Emulation.clearIdleOverride());
        devTools.send(Emulation.clearGeolocationOverride());
        devTools.send(Emulation.clearDeviceMetricsOverride());
        devTools.send(Emulation.setDeviceMetricsOverride(profile.getDeviceWidth(), profile.getDeviceHeight(), 1, profile.isMobile(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty()));
        devTools.send(Emulation.setHardwareConcurrencyOverride(profile.getHardwareConcurrency()));
        devTools.send(Emulation.setTouchEmulationEnabled(true, Optional.of(profile.getMaxTouchPoints())));
        devTools.send(Emulation.setUserAgentOverride(profile.getUserAgent(),
                Optional.of(profile.getAcceptLanguage()),
                Optional.empty(),
                Optional.of(new UserAgentMetadata(
                        Optional.of(profile.getBrandVersions()),
                        Optional.empty(),
                        Optional.of(profile.getFullVersion()),
                        profile.getPlatform(),
                        profile.getPlatformVersion(),
                        profile.getArchitecture(),
                        profile.getModel(),
                        profile.isMobile(),
                        Optional.empty(),
                        Optional.empty()
                ))));
    }

    private static void disableMedia(DevTools devTools) {
        List<String> blockedUrls = new ArrayList<>();
        blockedUrls.add("*://*/*.bmp");
        blockedUrls.add("*://*/*.gif");
        blockedUrls.add("*://*/*.png");
        blockedUrls.add("*://*/*.jpg");
        blockedUrls.add("*://*/*.jpeg");
        blockedUrls.add("*://*/*.webp");
        devTools.send(Network.setBlockedURLs(blockedUrls));
    }

    @NonNull
    private static ChromeOptions createChromeOptions(@NonNull String localProxy) {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setAcceptInsecureCerts(true);
        chromeOptions.addArguments("--headless", "--disable-gpu");
        chromeOptions.addArguments("--window-size=1920,1080");

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
    private ScrapResult getScrapResult(@NonNull ScrapRequest request, @NonNull ChromeDriver driver) {
        try {
            configureScreenSize(driver);
            String title = driver.getTitle();
            String finalUrl = driver.getCurrentUrl();
            String defaultScreenshotName = screenshotManager.takeDefaultScreenshot(request, driver);
            String screenshotWithoutMediaName = screenshotManager.takeScreenshotWithoutMedia(request, driver);
            String pageSource = driver.getPageSource();
            String jsVariables = scriptsManager.getJsVariables(request, driver);
            return new ScrapResult(request, title, finalUrl, defaultScreenshotName, screenshotWithoutMediaName, pageSource, jsVariables);
        } catch (RuntimeException e) {
            LOGGER.error("Failed to get scrap results for request [{}]", request, e);
            return new ScrapResult(request, "get results error");
        }
    }
    public void configureScreenSize(@NonNull ChromeDriver driver) {
        driver.getDevTools().send(
                Emulation.setDeviceMetricsOverride(1920, 1080, 1, true, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty()));
    }
}
