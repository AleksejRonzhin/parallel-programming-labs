package ru.rsreu.labs.tasks;

import ru.rsreu.labs.exceptions.TaskNotFoundException;

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

    public void start(int id) throws TaskNotFoundException {
        Thread thread = getByTaskId(id);
        thread.start();
    }

    public void stop(int id) throws TaskNotFoundException {
        Thread thread = getByTaskId(id);
        thread.stop();
    }

    public void stopAll() {
        threads.values().forEach(Thread::stop);
    }

    public void await(int id) throws TaskNotFoundException {
        Thread thread = getByTaskId(id);
        while (thread.isAlive()) {

        }
    }

    private Thread getByTaskId(int id) throws TaskNotFoundException {
        Thread thread = threads.get(id);
        if (thread == null) {
            throw new TaskNotFoundException();
        }
        return thread;
    }
}