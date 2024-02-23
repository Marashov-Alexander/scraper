package study.polytech.scraper;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueuesConfiguration {

    @Bean
    public ConfigurableQueue<String> urlsQueue() {
        return new ConfigurableQueue<>();
    }

    @Bean
    public ConfigurableQueue<ScrapResult> openedPagesQueue() {
        return new ConfigurableQueue<>();
    }

}
