package ru.rsreu.labs.concurrent;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;

public class SemaphoreTest {
    @Test
    public void test() throws InterruptedException {
        int threadCount = 100;
        int permits = 10;
        Semaphore semaphore = new Semaphore(permits);
        Counter workingTreadCounter = new Counter(permits);
        Collection<Thread> threads = createSemaphoreThreads(threadCount, semaphore, workingTreadCounter);
        ThreadUtils.startThreads(threads);
        ThreadUtils.joinThreads(threads);
        Assertions.assertFalse(workingTreadCounter.isWasCounterOverfull());
    }

    private Collection<Thread> createSemaphoreThreads(int threadCount, Semaphore semaphore, Counter workingTreadCounter) {
        Collection<Thread> threads = new ArrayList<>();
        for (int i = 0; i < threadCount; i++) {
            threads.add(new Thread(() -> {
                try {
                    semaphore.acquire();
                    workingTreadCounter.increment();
                    Thread.sleep(1000);
                    workingTreadCounter.decrement();
                    semaphore.release();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }));
        }
        return threads;
    }



    @Test
    public void acquireTest() throws InterruptedException {
        Semaphore semaphore = new Semaphore(2);
        semaphore.acquire();
        Assertions.assertEquals(1, semaphore.availablePermits());
    }

    @Test
    public void acquireAwaitingTest() throws InterruptedException {
        Semaphore semaphore = new Semaphore(0);
        Thread thread = new Thread(() -> {
            try {
                semaphore.acquire();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        thread.start();
        Thread.sleep(1000);
        Assertions.assertEquals(Thread.State.WAITING, thread.getState());
    }

    @Test
    public void tryAcquireTest() {
        Semaphore semaphore = new Semaphore(1);
        Assertions.assertTrue(semaphore.tryAcquire());
    }

    @Test
    public void tryAcquireEmptySemaphoreTest() {
        Semaphore semaphore = new Semaphore(0);
        Assertions.assertFalse(semaphore.tryAcquire());
    }

    @Test
    public void releaseTest() throws InterruptedException {
        Semaphore semaphore = new Semaphore(1);
        semaphore.acquire();
        Assertions.assertEquals(0, semaphore.availablePermits());
        semaphore.release();
        Assertions.assertEquals(1, semaphore.availablePermits());
    }

    @Test
    public void releaseAwaitingTest() throws InterruptedException {
        Semaphore semaphore = new Semaphore(1);
        new Thread(() -> {
            try {
                semaphore.acquire();
                Thread.sleep(1000);
                semaphore.release();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();

        semaphore.acquire();
        Assertions.assertEquals(0, semaphore.availablePermits());
    }
}
