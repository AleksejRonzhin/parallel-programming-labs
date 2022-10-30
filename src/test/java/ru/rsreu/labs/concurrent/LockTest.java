package ru.rsreu.labs.concurrent;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;

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
        for (int i = 0; i < threadsCount; i++){
            threads.add(new Thread(() -> {
                try {
                    for(int j = 0; j < summarizingCount; j++){
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
    public void tryLockTest(){
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
