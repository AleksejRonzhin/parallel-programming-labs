package ru.rsreu.labs.tasks.commands;

import ru.rsreu.labs.exceptions.TaskIsOverException;
import ru.rsreu.labs.exceptions.TaskNotFoundException;
import ru.rsreu.labs.tasks.ThreadRepo;

public class StopCommand extends ThreadCommand {
    private final int threadId;

    public StopCommand(ThreadRepo repo, int threadId) {
        super(repo);
        this.threadId = threadId;
    }

    @Override
    public void execute() throws TaskNotFoundException, TaskIsOverException {
        repo.stop(threadId);
        System.out.printf("Stopped task %d\n", threadId);
    }
}
