package ru.rsreu.labs.tasks.commands;

import ru.rsreu.labs.exceptions.TaskNotFoundException;
import ru.rsreu.labs.tasks.TaskRepository;

public class StartCommand extends ThreadCommand {
    private final int taskId;

    public StartCommand(TaskRepository repo, int taskId) {
        super(repo);
        this.taskId = taskId;
    }

    @Override
    public void execute() throws TaskNotFoundException {
        this.repo.start(taskId);
        System.out.printf("Started task %d\n", taskId);
    }
}