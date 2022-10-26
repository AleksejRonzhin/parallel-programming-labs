package ru.rsreu.labs.concurrent;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.rsreu.labs.concurrent.CountDownLatch;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CountDownLatchTest {
    @Test
    public void stressTest() throws InterruptedException {

    }

    @Test
    public void decrementCountTest() {
        CountDownLatch countDownLatch = new CountDownLatch(5);
        countDownLatch.countDown();
        Assertions.assertEquals(4, countDownLatch.getCount());
    }

    @Test
    public void decrementCountToMuchTest() {
        CountDownLatch countDownLatch = new CountDownLatch(2);
        for (int i = 0; i < 10; i++) {
            countDownLatch.countDown();
        }
        Assertions.assertEquals(0, countDownLatch.getCount());
    }

    @Test
    public void awaitTest() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Thread thread = new Thread(() -> {
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        thread.start();
        Thread.sleep(1000);

        Assertions.assertEquals(Thread.State.WAITING, thread.getState());
    }

    @Test
    public void awaitWithTimeoutTest() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Thread thread = new Thread(() -> {
            try {
                countDownLatch.await(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        thread.start();
        Thread.sleep(1000);

        Assertions.assertEquals(Thread.State.TERMINATED, thread.getState());
    }

    @Test
    public void interruptAwaitingTest() {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Thread thread = new Thread(() -> Assertions.assertThrowsExactly(InterruptedException.class, countDownLatch::await));
        thread.start();
        thread.interrupt();
    }
}
