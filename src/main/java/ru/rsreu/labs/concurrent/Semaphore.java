package ru.rsreu.labs.concurrent;

public class Semaphore {
    private final int maxPermits;
    private final Object lock = new Object();
    private int permits;


    public Semaphore(int permits) {
        this.maxPermits = permits;
        this.permits = permits;
    }

    public int availablePermits() {
        synchronized (lock) {
            return permits;
        }
    }

    public void acquire() throws InterruptedException {
        synchronized (lock) {
            while (permits == 0) {
                lock.wait();
            }
            permits--;
        }
    }

    public boolean tryAcquire() {
        synchronized (lock) {
            if (permits == 0) {
                return false;
            }
            permits--;
            return true;
        }
    }

    public void release() {
        synchronized (lock) {
            if (permits < maxPermits) {
                permits++;
                lock.notifyAll();
            }
        }
    }
}
