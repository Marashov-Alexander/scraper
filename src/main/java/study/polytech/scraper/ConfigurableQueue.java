package study.polytech.scraper;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class ConfigurableQueue<T> {

    private static final int DEFAULT_QUEUE_CAPACITY = 16;
    private static final long DEFAULT_PUSH_TIMEOUT_MILLIS = 100L;
    private static final long DEFAULT_POP_TIMEOUT_MILLIS = 1000L;

    private final long pushTimeoutMillis;
    private final BlockingQueue<T> queue;

    public ConfigurableQueue() {
        this(DEFAULT_QUEUE_CAPACITY, DEFAULT_PUSH_TIMEOUT_MILLIS);
    }

    public ConfigurableQueue(int queueCapacity, long pushTimeoutMillis) {
        this.pushTimeoutMillis = pushTimeoutMillis;
        this.queue = new ArrayBlockingQueue<>(queueCapacity);
    }

    public void push(@NonNull T element) throws QueueIsFullException {
        Objects.requireNonNull(element);
        try {
            if (queue.offer(element, pushTimeoutMillis, TimeUnit.MILLISECONDS)) {
                return;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        throw new QueueIsFullException();
    }

    @Nullable
    public T pop() throws InterruptedException {
        return queue.poll(DEFAULT_POP_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
    }
}
