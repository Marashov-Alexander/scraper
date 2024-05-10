package study.polytech.scraper.proxy;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.List;

public interface ProxyManagerConfiguration {

    List<String> getLocalProxies();

    @Configuration
    class Provider {
        @Bean
        public ProxyManagerConfiguration proxyManagerConfiguration() {
            return () -> Collections.singletonList("localhost:3128");
        }
    }
}
