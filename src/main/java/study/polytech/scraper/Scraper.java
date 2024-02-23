package study.polytech.scraper;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.http.ClientConfig;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class Scraper {

    @NonNull
    public String scrap(@NonNull String url) {
        WebDriver driver = RemoteWebDriver.builder()
                .addAlternative(new ChromeOptions())
                .build();
        driver.get(url);
        String result = driver.getTitle();
        driver.close();
        return result;
    }

}
