package study.polytech.scraper;

import jakarta.annotation.PreDestroy;
import org.springframework.lang.NonNull;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public abstract class QueueElementsConsumer<T> {

    private static final int DEFAULT_POOL_SIZE = 1;
    private static final long AWAIT_TERMINATION_TIMEOUT_MILLIS = 5_000L;

    private final ConfigurableQueue<T> queue;
    private final ExecutorService executor;
    private volatile boolean terminated;

    public QueueElementsConsumer(@NonNull ConfigurableQueue<T> queue) {
        this(queue, DEFAULT_POOL_SIZE);
    }

    public QueueElementsConsumer(@NonNull ConfigurableQueue<T> queue, int poolSize) {
        Objects.requireNonNull(queue);
        this.queue = queue;
        this.executor = Executors.newFixedThreadPool(poolSize);
        this.terminated = false;
        for (int i = 0; i < DEFAULT_POOL_SIZE; i++) {
            executor.submit(this::startProcess);
        }
    }

    @PreDestroy
    public void stop() {
        terminated = true;
        executor.shutdown();
        try {
            executor.awaitTermination(AWAIT_TERMINATION_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (!executor.isTerminated()) {
            executor.shutdownNow();
        }
    }

    protected abstract void process(@NonNull T element);

    private void startProcess() {
        while (!terminated) {
            try {
                T element = queue.pop();
                if (element != null) {
                    process(element);
                }
            } catch (InterruptedException | RuntimeException e) {
                e.printStackTrace();
            }
        }
    }
}
