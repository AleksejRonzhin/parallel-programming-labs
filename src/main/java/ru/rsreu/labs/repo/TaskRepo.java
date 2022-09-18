package ru.rsreu.labs.repo;

import ru.rsreu.labs.exceptions.TaskNotFoundException;

import java.util.HashMap;
import java.util.Map;

public class TaskRepo {
    private final Map<Integer, Thread> tasks = new HashMap<>();

    public int create(Runnable target) {
        int id = tasks.size();
        String name = "thread " + id;
        Thread thread = new Thread(target, name);
        thread.setUncaughtExceptionHandler((t, e) -> System.out.printf("Exception in %s: %s\n", t.getName(), e));
        tasks.put(id, thread);
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

    public void stopAll(){
        tasks.values().forEach(Thread::stop);
    }

    public void await(int id) throws TaskNotFoundException {
        Thread thread = getByTaskId(id);
        while (thread.isAlive()) {

        }
    }

    private Thread getByTaskId(int id) throws TaskNotFoundException {
        Thread thread = tasks.get(id);
        if(thread == null){
            throw new TaskNotFoundException();
        }
        return thread;
    }
}