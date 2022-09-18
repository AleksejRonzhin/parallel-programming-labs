package ru.rsreu.labs.commands;

import ru.rsreu.labs.exceptions.TaskNotFoundException;
import ru.rsreu.labs.repo.TaskRepo;

public class StopCommand extends TaskCommand {
    private final int threadId;
    protected StopCommand(TaskRepo repo, int threadId) {
        super(repo);
        this.threadId = threadId;
    }

    @Override
    public void execute() throws TaskNotFoundException {
        repo.stop(threadId);
        System.out.printf("Stopped thread %d\n", threadId);
    }
}
