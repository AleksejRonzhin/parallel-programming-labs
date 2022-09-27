package ru.rsreu.labs.tasks;

public abstract class TaskCreator {
    protected ThreadRepo repo;

    protected TaskCreator(ThreadRepo repo) {
        this.repo = repo;
    }

    public ThreadRepo getRepo() {
        return repo;
    }

    public abstract int create(String[] args);
}