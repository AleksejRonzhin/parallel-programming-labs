package ru.rsreu.labs.tasks.pi;

import ru.rsreu.labs.Logger;
import ru.rsreu.labs.tasks.TaskProgress;
import ru.rsreu.labs.tasks.TaskCreator;
import ru.rsreu.labs.tasks.ThreadRepo;

public class PiCalculatingTaskCreator extends TaskCreator {
    private static final double STEP = 1E-9;
    private final Logger logger;

    public PiCalculatingTaskCreator(Logger logger, ThreadRepo repo) {
        super(repo);
        this.logger = logger;
    }

    public int create(String[] args) {
        TaskProgress<Double> taskProgress = new TaskProgress<>();
        logger.logProgress(taskProgress);

        Runnable target = () -> {
            int radius = Integer.parseInt(args[0]);
            new CircleAreaPiCalculator(STEP, radius).calculate(taskProgress);
        };
        int taskId = this.repo.create(target);
        taskProgress.setTaskName(String.format("Pi calculating task %d", taskId));
        return taskId;
    }
}