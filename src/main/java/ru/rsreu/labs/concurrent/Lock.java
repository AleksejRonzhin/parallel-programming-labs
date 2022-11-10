package ru.rsreu.labs.concurrent;

public class Lock {
    private final Object lock = new Object();
    private boolean isLocked = false;

    public boolean isLocked() {
        synchronized (lock){
            return isLocked;
        }
    }

    public void lock() throws InterruptedException {
        synchronized (lock){
            while(isLocked){
                lock.wait();
            }
            isLocked = true;
        }
    }

    public boolean tryLock(){
        synchronized (lock){
            if(isLocked){
                return false;
            }
            isLocked = true;
            return true;
        }
    }

    public void unlock(){
        synchronized (lock){
            isLocked = false;
            lock.notifyAll();
        }
    }
}