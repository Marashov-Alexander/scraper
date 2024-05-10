package study.polytech.scraper;

import com.google.common.collect.Lists;
import org.openqa.selenium.devtools.v118.emulation.model.UserAgentBrandVersion;
import org.springframework.lang.NonNull;

import java.util.List;

public class ScraperProfile {

    public static final ScraperProfile DEFAULT = new ScraperProfile(
            393,
            873,
            8,
            5,
            "Mozilla/5.0 (Linux; Android 10; K) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Mobile Safari/537.36",
            "en-US,en;q=0.9,ru-RU;q=0.8,ru;q=0.7",
            Lists.newArrayList(
                    new UserAgentBrandVersion("Chromium", "124"),
                    new UserAgentBrandVersion("Google Chrome", "124"),
                    new UserAgentBrandVersion("Not-A.Brand", "99")
            ),
            "124.0.6367.114",
            "Android",
            "13.0.0",
            "",
            "M2101K6G",
            true
    );

    private final int deviceWidth;
    private final int deviceHeight;
    private final int hardwareConcurrency;
    private final int maxTouchPoints;
    private final String userAgent;
    private final String acceptLanguage;
    private final List<UserAgentBrandVersion> brandVersions;
    private final String fullVersion;
    private final String platform;
    private final String platformVersion;
    private final String architecture;
    private final String model;
    private final boolean isMobile;

    public ScraperProfile(int deviceWidth,
                          int deviceHeight,
                          int hardwareConcurrency,
                          int maxTouchPoints,
                          @NonNull String userAgent,
                          @NonNull String acceptLanguage,
                          @NonNull List<UserAgentBrandVersion> brandVersions,
                          @NonNull String fullVersion,
                          @NonNull String platform,
                          @NonNull String platformVersion,
                          @NonNull String architecture,
                          @NonNull String model,
                          boolean isMobile) {
        this.deviceWidth = deviceWidth;
        this.deviceHeight = deviceHeight;
        this.hardwareConcurrency = hardwareConcurrency;
        this.maxTouchPoints = maxTouchPoints;
        this.userAgent = userAgent;
        this.acceptLanguage = acceptLanguage;
        this.brandVersions = brandVersions;
        this.fullVersion = fullVersion;
        this.platform = platform;
        this.platformVersion = platformVersion;
        this.architecture = architecture;
        this.model = model;
        this.isMobile = isMobile;
    }

    public int getDeviceWidth() {
        return deviceWidth;
    }

    public int getDeviceHeight() {
        return deviceHeight;
    }

    public int getHardwareConcurrency() {
        return hardwareConcurrency;
    }

    public int getMaxTouchPoints() {
        return maxTouchPoints;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public String getAcceptLanguage() {
        return acceptLanguage;
    }

    public List<UserAgentBrandVersion> getBrandVersions() {
        return brandVersions;
    }

    public String getFullVersion() {
        return fullVersion;
    }

    public String getPlatform() {
        return platform;
    }

    public String getPlatformVersion() {
        return platformVersion;
    }

    public String getArchitecture() {
        return architecture;
    }

    public String getModel() {
        return model;
    }

    public boolean isMobile() {
        return isMobile;
    }
}
