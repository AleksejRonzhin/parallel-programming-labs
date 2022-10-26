package ru.rsreu.labs.concurrent;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SemaphoreTest {
    @Test
    public void stressTest(){

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
        Thread thread = new Thread(()-> {
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
    public void tryAcquireTest(){
        Semaphore semaphore = new Semaphore(1);
        Assertions.assertTrue(semaphore.tryAcquire());
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
