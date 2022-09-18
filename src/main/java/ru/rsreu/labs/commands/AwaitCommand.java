package ru.rsreu.labs.commands;

import ru.rsreu.labs.exceptions.TaskNotFoundException;
import ru.rsreu.labs.repo.TaskRepo;

public class AwaitCommand extends TaskCommand {
    private final int threadId;

    protected AwaitCommand(TaskRepo repo, int threadId) {
        super(repo);
        this.threadId = threadId;
    }

    @Override
    public void execute() throws TaskNotFoundException {
        System.out.printf("Start waiting task %d\n", threadId);
        repo.await(threadId);
        System.out.println("The wait is over");
    }
}
