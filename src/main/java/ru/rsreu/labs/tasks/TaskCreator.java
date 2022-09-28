package ru.rsreu.labs.tasks;

import ru.rsreu.labs.exceptions.BadArgsException;

public abstract class TaskCreator {
    protected ThreadRepo repo;

    protected TaskCreator(ThreadRepo repo) {
        this.repo = repo;
    }

    public ThreadRepo getRepo() {
        return repo;
    }

    public abstract int create(String[] args) throws BadArgsException;
}