package ru.rsreu.labs.repo;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ThreadRepo {
    private final Map<Integer, Thread> threads = new HashMap<>();

    public int create(Runnable target) {
        int id = threads.size();
        String name = "thread " + id;
        Thread thread = new Thread(target, name);
        thread.setUncaughtExceptionHandler((t, e) -> System.out.printf("Exception in %s: %s\n", t.getName(), e));
        threads.put(id, thread);
        return id;
    }

    public void start(int id) {
        Thread thread = getById(id);
        thread.start();
    }

    public Thread getById(int id) {
        return threads.get(id);
    }

    public void stop(int id) {
        Thread thread = getById(id);
        thread.stop();
    }

    public void await(int id) {
        Thread thread = getById(id);
        while (thread.isAlive()) {

        }
    }
}
