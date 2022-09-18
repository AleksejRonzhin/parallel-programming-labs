package ru.rsreu.labs.commands;

import ru.rsreu.labs.exceptions.TaskNotFoundException;
import ru.rsreu.labs.repo.TaskRepo;

public class StartCommand extends TaskCommand {
    private final Runnable runnable;

    public StartCommand(TaskRepo repo, Runnable runnable) {
        super(repo);
        this.runnable = runnable;
    }

    @Override
    public void execute() throws TaskNotFoundException {
        int id = this.repo.create(runnable);
        this.repo.start(id);
        System.out.printf("Started task %d\n", id);
    }
}
