package ru.rsreu.labs.tasks;

import ru.rsreu.labs.exceptions.TaskIsOverException;
import ru.rsreu.labs.exceptions.TaskNotFoundException;

import java.util.HashMap;
import java.util.Map;

public class ThreadRepo {
    private final Map<Integer, Thread> threads = new HashMap<>();

    public int create(Runnable target) {
        int id = threads.size();
        String name = "thread " + id;
        Thread thread = new Thread(target, name);
        //thread.setUncaughtExceptionHandler((t, e) -> System.out.printf("Exception in %s: %s\n", t.getName(), e));
        threads.put(id, thread);
        return id;
    }

    public void start(int id) throws TaskNotFoundException {
        Thread thread = getByTaskId(id);
        thread.start();
    }

    public void stop(int id) throws TaskNotFoundException, TaskIsOverException {
        Thread thread = getAliveThread(id);
        thread.interrupt();
    }

    public void stopAll() {
        threads.values().forEach(Thread::interrupt);
    }

    public void await(int id) throws TaskNotFoundException, TaskIsOverException {
        Thread thread = getAliveThread(id);
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

    private Thread getAliveThread(int taskId) throws TaskNotFoundException, TaskIsOverException {
        Thread thread = getByTaskId(taskId);
        if (!thread.isAlive()) throw new TaskIsOverException();
        return thread;
    }
}