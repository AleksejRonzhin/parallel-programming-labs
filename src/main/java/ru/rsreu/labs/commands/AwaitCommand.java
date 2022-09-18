package ru.rsreu.labs.commands;

import ru.rsreu.labs.repo.ThreadRepo;

public class AwaitCommand extends ThreadCommand {
    private final int threadId;

    protected AwaitCommand(ThreadRepo repo, int threadId) {
        super(repo);
        this.threadId = threadId;
    }

    @Override
    public void execute() {
        System.out.printf("Await thread %d\n", threadId);
        repo.await(threadId);
        System.out.println("The wait is over");
    }
}
