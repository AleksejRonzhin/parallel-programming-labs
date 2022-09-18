package ru.rsreu.labs.commands;

import ru.rsreu.labs.repo.ThreadRepo;

public class StopCommand extends ThreadCommand {
    private final int threadId;
    protected StopCommand(ThreadRepo repo, int threadId) {
        super(repo);
        this.threadId = threadId;
    }

    @Override
    public void execute() {
        repo.stop(threadId);
        System.out.printf("Stopped thread %d\n", threadId);
    }
}
