package study.polytech.scraper;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class OutputResultsHandler {

    private final Map<String, String> urlToResult;
    private final ReentrantLock lock;
    private final Condition condition;

    public OutputResultsHandler() {
        this.urlToResult = new HashMap<>();
        this.lock = new ReentrantLock();
        this.condition = lock.newCondition();
    }

    public void putResults(@NonNull String url, @NonNull String results) {
        lock.lock();
        try {
            urlToResult.put(url, results);
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }

    @Nullable
    public String waitForResults(@NonNull String url, long timeoutMillis) {
        lock.lock();
        try {
            return awaitResults(url, timeoutMillis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return null;
    }

    @Nullable
    private String awaitResults(@NonNull String url, long timeoutMillis) throws InterruptedException {
        long nanosRemaining = TimeUnit.MILLISECONDS.toNanos(timeoutMillis);
        String result;
        while ((result = urlToResult.get(url)) == null) {
            if (nanosRemaining <= 0L) {
                return null;
            }
            nanosRemaining = condition.awaitNanos(nanosRemaining);
        }
        return result;
    }
}
