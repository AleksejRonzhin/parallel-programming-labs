package ru.rsreu.labs.tasks;

import ru.rsreu.labs.exceptions.BadArgsException;

public abstract class TaskCreator {
    protected TaskRepository repo;

    protected TaskCreator(TaskRepository repo) {
        this.repo = repo;
    }

    public TaskRepository getRepo() {
        return repo;
    }

    public abstract int create(String[] args) throws BadArgsException;
}