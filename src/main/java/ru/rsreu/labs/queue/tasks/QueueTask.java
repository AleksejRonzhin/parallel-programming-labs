package ru.rsreu.labs.queue.tasks;

import java.util.concurrent.CountDownLatch;

public class QueueTask<T> {
    private final CountDownLatch latch = new CountDownLatch(1);
    private T result;

    public void setResult(T result) {
        this.result = result;
        latch.countDown();
    }

    public T awaitResult() throws InterruptedException {
        latch.await();
        return result;
    }
}
