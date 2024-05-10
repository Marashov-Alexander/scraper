package study.polytech.scraper.queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class ConfigurableQueue<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurableQueue.class);

    private static final int DEFAULT_QUEUE_CAPACITY = 16;
    private static final long DEFAULT_PUSH_TIMEOUT_MILLIS = 100L;
    private static final long DEFAULT_POP_TIMEOUT_MILLIS = 1000L;

    private final String queueName;
    private final long pushTimeoutMillis;
    private final BlockingQueue<T> queue;

    public ConfigurableQueue(@NonNull String queueName) {
        this(queueName, DEFAULT_QUEUE_CAPACITY, DEFAULT_PUSH_TIMEOUT_MILLIS);
    }

    public ConfigurableQueue(@NonNull String queueName, int queueCapacity, long pushTimeoutMillis) {
        this.queueName = queueName;
        this.pushTimeoutMillis = pushTimeoutMillis;
        this.queue = new ArrayBlockingQueue<>(queueCapacity);
    }

    public void push(@NonNull T element) throws QueueIsFullException {
        Objects.requireNonNull(element);
        try {
            if (queue.offer(element, pushTimeoutMillis, TimeUnit.MILLISECONDS)) {
                LOGGER.info("Element [{}] added to queue [{}]", element, queueName);
                return;
            }
        } catch (InterruptedException e) {
            LOGGER.error("Offer element [{}] operation interrupted for queue [{}]", element, queueName, e);
        }
        QueueIsFullException exception = new QueueIsFullException();
        LOGGER.error("Unable to add element [{}] to queue [{}]", element, queueName, exception);
        throw exception;
    }

    @Nullable
    public T pop() throws InterruptedException {
        long start = System.currentTimeMillis();
        T element = queue.poll(DEFAULT_POP_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
        if (element == null) {
            LOGGER.debug("No elements in queue [{}], took [{}] ms", queueName, System.currentTimeMillis() - start);
        } else {
            LOGGER.info("Element [{}] removed from queue [{}], took [{}] ms", element, queueName, System.currentTimeMillis() - start);
        }
        return element;
    }

    @NonNull
    public String getQueueName() {
        return queueName;
    }
}
