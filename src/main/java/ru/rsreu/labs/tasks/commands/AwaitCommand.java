package ru.rsreu.labs.tasks.commands;

import ru.rsreu.labs.exceptions.TaskIsOverException;
import ru.rsreu.labs.exceptions.TaskNotFoundException;
import ru.rsreu.labs.tasks.TaskRepository;

public class AwaitCommand extends ThreadCommand {
    private final int taskId;

    public AwaitCommand(TaskRepository repo, int threadId) {
        super(repo);
        this.taskId = threadId;
    }

    @Override
    public void execute() throws TaskNotFoundException, TaskIsOverException {
        try {
            repo.await(taskId);
            System.out.printf("The wait task %d is over\n", taskId);
        } catch (InterruptedException e) {
            System.out.printf("The task %d is interrupted\n", taskId);
        }
    }
}