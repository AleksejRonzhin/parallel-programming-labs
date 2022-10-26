package ru.rsreu.labs.concurrent;

public class CountDownLatch {
    private final Object lock = new Object();
    private int count;

    public CountDownLatch(int count) {
        this.count = count;
    }

    public int getCount() {
        synchronized (lock) {
            return count;
        }
    }

    public void countDown() {
        if (count == 0) return;
        synchronized (lock) {
            if (count == 0) return;

            count--;
            if (count == 0) {
                lock.notifyAll();
            }
        }
    }

    public void await() throws InterruptedException {
        if (count == 0) return;
        synchronized (lock) {
            if (count == 0) return;
            lock.wait();
        }
    }

    public void await(long timeout) throws InterruptedException {
        if (count == 0) return;
        synchronized (lock) {
            if (count == 0) return;
            lock.wait(timeout);
        }
    }
}