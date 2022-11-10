package ru.rsreu.labs.concurrent;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class LockTest {
    @Test
    public void test() throws InterruptedException {
        int threadsCount = 10;
        int summarizingCount = 1000000;
        Lock lock = new Lock();
        SumStorage sumStorage = new SumStorage();
        Collection<Thread> threads = createSummarizingThreads(threadsCount, sumStorage, lock, summarizingCount);
        ThreadUtils.startThreads(threads);
        ThreadUtils.joinThreads(threads);
        Assertions.assertEquals(threadsCount * summarizingCount, sumStorage.getSum());
    }

    private Collection<Thread> createSummarizingThreads(int threadsCount, SumStorage sumStorage, Lock lock, int summarizingCount) {
        Collection<Thread> threads = new ArrayList<>();
        for (int i = 0; i < threadsCount; i++) {
            threads.add(new Thread(() -> {
                try {
                    for (int j = 0; j < summarizingCount; j++) {
                        lock.lock();
                        sumStorage.add(1);
                        lock.unlock();
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }));
        }
        return threads;
    }

    @Test
    public void twoThreadTest() throws InterruptedException {
        CountDownLatch firstThreadCdl = new CountDownLatch(1);
        CountDownLatch secondThreadCdl = new CountDownLatch(1);
        AtomicInteger atomicInteger = new AtomicInteger(0);
        Lock lock = new Lock();

        Thread firstThread = getFirstThread(lock, firstThreadCdl, atomicInteger);
        Thread secondThread = getSecondThread(lock, secondThreadCdl, atomicInteger);
        firstThread.start();
        secondThread.start();

        firstThreadCdl.await();
        Assertions.assertEquals(1, atomicInteger.get());
        secondThreadCdl.countDown();

        secondThread.join();
        Assertions.assertEquals(2, atomicInteger.get());
    }

    public Thread getFirstThread(Lock lock, CountDownLatch countDownLatch, AtomicInteger atomicInteger) {
        return new Thread(() -> {
            try {
                lock.lock();
                atomicInteger.set(1);
                countDownLatch.countDown();
                lock.unlock();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public Thread getSecondThread(Lock lock, CountDownLatch countDownLatch, AtomicInteger atomicInteger) {
        return new Thread(() -> {
            try {
                countDownLatch.await();
                lock.lock();
                atomicInteger.set(2);
                lock.unlock();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Test
    public void lockTest() throws InterruptedException {
        Lock lock = new Lock();
        lock.lock();
        Assertions.assertTrue(lock.isLocked());
    }

    @Test
    public void unlockTest() throws InterruptedException {
        Lock lock = new Lock();
        lock.lock();
        lock.unlock();
        Assertions.assertFalse(lock.isLocked());
    }

    @Test
    public void tryLockTest() {
        Lock lock = new Lock();
        Assertions.assertTrue(lock.tryLock());
    }

    @Test
    public void tryLockIsLockedLockTest() throws InterruptedException {
        Lock lock = new Lock();
        lock.lock();
        Assertions.assertFalse(lock.tryLock());
    }
}
