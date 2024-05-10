package study.polytech.scraper.queue;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.apache.tomcat.util.threads.TaskThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public abstract class QueueElementsConsumer<T> {

    private static final int DEFAULT_POOL_SIZE = 1;
    private static final long AWAIT_TERMINATION_TIMEOUT_MILLIS = 5_000L;

    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private final ConfigurableQueue<T> queue;
    private final ExecutorService executor;
    private final int poolSize;
    private volatile boolean terminated;

    public QueueElementsConsumer(@NonNull ConfigurableQueue<T> queue) {
        this(queue, DEFAULT_POOL_SIZE);
    }

    public QueueElementsConsumer(@NonNull ConfigurableQueue<T> queue, int poolSize) {
        Objects.requireNonNull(queue);
        this.queue = queue;
        this.executor = Executors.newFixedThreadPool(poolSize, createThreadFactory(queue));
        this.poolSize = poolSize;
        this.terminated = false;
    }

    @PostConstruct
    public void start() {
        for (int i = 0; i < poolSize; i++) {
            executor.submit(this::startProcess);
        }
        logger.info("Consumer started with pool size [{}]", poolSize);
    }

    @PreDestroy
    public void stop() {
        terminated = true;
        executor.shutdown();

        try {
            boolean terminated = executor.awaitTermination(AWAIT_TERMINATION_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
            logger.info("Is consumer terminated in required deadline: [{}]", terminated);
        } catch (InterruptedException e) {
            logger.error("Awaiting for termination interrupted", e);
        }

        if (executor.isTerminated()) {
            logger.info("Consumer stopped successfully");
        } else {
            executor.shutdownNow();
            logger.warn("Consumer stopped successfully with shutdownNow invocation");
        }
    }

    protected abstract void process(@NonNull T element);

    private void startProcess() {
        String threadName = Thread.currentThread().getName();
        logger.info("Consumer thread [{}] started", threadName);
        while (!terminated) {
            try {
                T element = queue.pop();
                if (element != null) {
                    logger.info("Consumer thread [{}] consumes element [{}]", threadName, element);
                    process(element);
                    logger.info("Consumer thread [{}] successfully processed element [{}]", threadName, element);
                }
            } catch (InterruptedException e) {
                logger.error("Consumer thread [{}] interrupted", threadName, e);
            } catch (RuntimeException e) {
                logger.error("Consumer thread [{}] failed to consume queue element", threadName, e);
            }
        }
        logger.info("Consumer thread [{}] finished", threadName);
    }

    @NonNull
    private static TaskThreadFactory createThreadFactory(ConfigurableQueue<?> queue) {
        return new TaskThreadFactory(queue.getQueueName() + "_consumer", false, 1);
    }
}
