package ru.rsreu.labs.concurrent;

public class Counter {
    private final int maxValue;
    private final Object lock = new Object();
    private int value = 0;
    private boolean wasCounterOverfull = false;


    public Counter(int maxValue) {
        this.maxValue = maxValue;
    }

    public void increment() {
        synchronized (lock) {
            value++;
            if (value > maxValue) wasCounterOverfull = true;
        }
    }

    public void decrement() {
        synchronized (lock) {
            value--;
        }
    }

    public boolean isWasCounterOverfull() {
        return wasCounterOverfull;
    }
}
