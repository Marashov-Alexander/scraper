package study.polytech.scraper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScraperApplication {

	public static void main(String[] args) {
		System.setProperty("webdriver.chrome.driver", "/usr/local/bin/chromedriver");
		System.setProperty("webdriver.chrome.logfile", "~/help/chromedriver.log");
		System.setProperty("webdriver.chrome.verboseLogging", "true");
		SpringApplication.run(ScraperApplication.class, args);
	}

}
