package study.polytech.scraper.resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Component
public class ProxyManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProxyManager.class);

    private static final long POLL_TIMEOUT = 60L;
    private final BlockingQueue<String> freeProxies;

    public ProxyManager(@NonNull ProxyManagerConfiguration proxyManagerConfiguration) {
        List<String> localProxies = proxyManagerConfiguration.getLocalProxies();
        this.freeProxies = new ArrayBlockingQueue<>(localProxies.size(), true, localProxies);
    }

    public <T> T doWithLocalProxy(@NonNull Function<String, T> function) {
        Objects.requireNonNull(function);
        String freeProxy = null;
        try {
            long startTime = System.currentTimeMillis();
            freeProxy = freeProxies.poll(POLL_TIMEOUT, TimeUnit.SECONDS);
            LOGGER.info("Found free proxy [{}], operation took [{}] ms", freeProxy, System.currentTimeMillis() - startTime);
            return function.apply(freeProxy);
        } catch (Exception e) {
            LOGGER.error("An error occurred", e);
        } finally {
            if (freeProxy != null && !freeProxies.offer(freeProxy)) {
                LOGGER.error("Unable to return proxy [{}] to proxies pool", freeProxy);
            }
        }
        return null;
    }

}
