package ru.rsreu.labs.tasks.commands;

import ru.rsreu.labs.exceptions.TaskIsOverException;
import ru.rsreu.labs.exceptions.TaskNotFoundException;
import ru.rsreu.labs.tasks.ThreadRepo;

public class AwaitCommand extends ThreadCommand {
    private final int threadId;

    public AwaitCommand(ThreadRepo repo, int threadId) {
        super(repo);
        this.threadId = threadId;
    }

    @Override
    public void execute() throws TaskNotFoundException, TaskIsOverException {
        repo.await(threadId);
        System.out.printf("The wait task %d is over\n", threadId);
    }
}