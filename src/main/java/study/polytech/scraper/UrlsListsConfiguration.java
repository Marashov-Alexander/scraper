package study.polytech.scraper;

import com.google.common.collect.Sets;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

@Configuration
public class UrlsListsConfiguration {

    @Bean
    public Set<String> whitelistUrls() {
        return Sets.newHashSet("https://vk.com");
    }

    @Bean
    public Set<String> blacklistUrls() {
        return Sets.newHashSet("https://bad-site.net");
    }

}
