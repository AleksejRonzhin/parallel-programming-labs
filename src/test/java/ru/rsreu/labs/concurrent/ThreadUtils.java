package ru.rsreu.labs.concurrent;

import java.util.Collection;

public class ThreadUtils {
    public static void startThreads(Collection<Thread> threads) {
        threads.forEach(Thread::start);
    }

    public static boolean allThreadsIsState(Collection<Thread> threads, Thread.State state) {
        for (Thread thread : threads) {
            if (thread.getState() != state) {
                return false;
            }
        }
        return true;
    }

    public  static void joinThreads(Collection<Thread> threads) throws InterruptedException {
        for (Thread thread : threads) {
            thread.join();
        }
    }
}
