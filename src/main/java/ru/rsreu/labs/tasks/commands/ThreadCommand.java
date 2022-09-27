package ru.rsreu.labs.tasks.commands;

import ru.rsreu.labs.commands.Command;
import ru.rsreu.labs.exceptions.TaskNotFoundException;
import ru.rsreu.labs.tasks.ThreadRepo;

public abstract class ThreadCommand implements Command {
    protected ThreadRepo repo;

    protected ThreadCommand(ThreadRepo repo){
        this.repo = repo;
    }

    @Override
    public abstract void execute() throws TaskNotFoundException;
}
