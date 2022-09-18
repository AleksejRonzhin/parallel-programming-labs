package ru.rsreu.labs.commands;

import ru.rsreu.labs.commands.ThreadCommand;
import ru.rsreu.labs.repo.ThreadRepo;

public class StartCommand extends ThreadCommand {
    private final Runnable runnable;

    public StartCommand(ThreadRepo repo, Runnable runnable) {
        super(repo);
        this.runnable = runnable;
    }

    @Override
    public void execute() {
        int id = this.repo.create(runnable);
        this.repo.start(id);
        System.out.printf("Started thread %d\n", id);
    }
}
