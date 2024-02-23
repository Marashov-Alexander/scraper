package study.polytech.scraper;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.http.ClientConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class Scraper {

    private static final Logger LOGGER = LoggerFactory.getLogger(Scraper.class);

    @NonNull
    public String scrap(@NonNull String url) {
        LOGGER.info("Scrapping url [{}] started", url);
        WebDriver driver = RemoteWebDriver.builder()
                .addAlternative(new ChromeOptions())
                .build();
        driver.get(url);
        String result = driver.getTitle();
        driver.close();
        LOGGER.info("Scrapping url [{}] finished, result is [{}]", url, result);
        return result;
    }

}
