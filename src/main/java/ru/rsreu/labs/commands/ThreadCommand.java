package ru.rsreu.labs.commands;

import ru.rsreu.labs.repo.ThreadRepo;

public abstract class ThreadCommand implements Command {
    protected ThreadRepo repo;

    protected ThreadCommand(ThreadRepo repo){
        this.repo = repo;
    }
}
