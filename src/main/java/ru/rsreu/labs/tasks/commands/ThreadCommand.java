package ru.rsreu.labs.tasks.commands;

import ru.rsreu.labs.commands.Command;
import ru.rsreu.labs.exceptions.TaskIsOverException;
import ru.rsreu.labs.exceptions.TaskNotFoundException;
import ru.rsreu.labs.tasks.TaskRepository;

public abstract class ThreadCommand implements Command {
    protected TaskRepository repo;

    protected ThreadCommand(TaskRepository repo){
        this.repo = repo;
    }

    @Override
    public abstract void execute() throws TaskNotFoundException, TaskIsOverException;
}
