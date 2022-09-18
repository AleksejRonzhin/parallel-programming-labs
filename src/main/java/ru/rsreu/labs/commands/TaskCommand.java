package ru.rsreu.labs.commands;

import ru.rsreu.labs.exceptions.TaskNotFoundException;
import ru.rsreu.labs.repo.TaskRepo;

public abstract class TaskCommand implements Command {
    protected TaskRepo repo;

    protected TaskCommand(TaskRepo repo){
        this.repo = repo;
    }

    @Override
    public abstract void execute() throws TaskNotFoundException;
}
