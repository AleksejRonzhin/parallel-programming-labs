package ru.rsreu.labs.tasks;

public class Task {
    private final int id;
    private String name;
    private Thread thread;

    public Task(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTarget(Runnable target) {
        this.thread = new Thread(target);
    }

    public void start() {
        this.thread.start();
    }

    public void stop() {
        this.thread.interrupt();
    }

    public void await() {
        while (thread.isAlive()) {
        }
    }

    public boolean isFinished() {
        if (this.thread == null) return false;
        return !this.thread.isAlive();
    }
}