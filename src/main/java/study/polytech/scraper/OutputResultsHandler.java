package study.polytech.scraper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import study.polytech.scraper.analyzer.ModerationResult;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class OutputResultsHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(OutputResultsHandler.class);

    private final Map<String, ModerationResult> urlToResult;
    private final ReentrantLock lock;
    private final Condition condition;

    public OutputResultsHandler() {
        this.urlToResult = new HashMap<>();
        this.lock = new ReentrantLock();
        this.condition = lock.newCondition();
    }

    public void putResults(@NonNull ModerationResult ModerationResult) {
        lock.lock();
        try {
            LOGGER.info("New results available [{}]", ModerationResult);
            urlToResult.put(ModerationResult.getUrl(), ModerationResult);
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }

    @Nullable
    public ModerationResult waitForResults(@NonNull String url, long timeoutMillis) {
        lock.lock();
        try {
            return awaitResults(url, timeoutMillis);
        } catch (InterruptedException e) {
            e.printStackTrace();
            LOGGER.error("Waiting for results for url [{}] interrupted", url);
            return null;
        } finally {
            lock.unlock();
        }
    }

    @Nullable
    private ModerationResult awaitResults(@NonNull String url, long timeoutMillis) throws InterruptedException {
        long start = System.currentTimeMillis();
        long nanosRemaining = TimeUnit.MILLISECONDS.toNanos(timeoutMillis);
        ModerationResult result;
        while ((result = urlToResult.get(url)) == null) {
            if (nanosRemaining <= 0L) {
                LOGGER.error("Waiting for results for url [{}] timed out", url);
                return null;
            }
            nanosRemaining = condition.awaitNanos(nanosRemaining);
        }
        LOGGER.info("Results for url [{}] are [{}], waiting took [{}] ms", url, result, System.currentTimeMillis() - start);
        return result;
    }
}
