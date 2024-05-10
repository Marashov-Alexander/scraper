package study.polytech.scraper.queue;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import study.polytech.scraper.ScrapRequest;
import study.polytech.scraper.analyzer.ScrapResult;

@Configuration
public class QueuesConfiguration {

    @Bean
    public ConfigurableQueue<ScrapRequest> urlsQueue() {
        return new ConfigurableQueue<>("urlsQueue");
    }

    @Bean
    public ConfigurableQueue<ScrapResult> openedPagesQueue() {
        return new ConfigurableQueue<>("openedPagesQueue");
    }

}
