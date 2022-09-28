package ru.rsreu.labs.tasks.pi;

import ru.rsreu.labs.exceptions.BadArgsException;
import ru.rsreu.labs.integrals.CircleAreaPiCalculator;
import ru.rsreu.labs.tasks.TaskCreator;
import ru.rsreu.labs.tasks.ThreadRepo;
import ru.rsreu.labs.tasks.progress.TaskProgressInfo;
import ru.rsreu.labs.tasks.progress.TaskProgressLogger;

public class PiCalculatingTaskWithLogCreator extends TaskCreator {
    private static final double STEP = 1E-9;
    private final TaskProgressLogger taskProgressLogger = new TaskProgressLogger();

    public PiCalculatingTaskWithLogCreator(ThreadRepo repo) {
        super(repo);
    }

    @Override
    public int create(String[] args) throws BadArgsException {
        try {
            double radius = Double.parseDouble(args[0]);
            TaskProgressInfo<Double> taskProgressInfo = new TaskProgressInfo<>();
            taskProgressLogger.logProgress(taskProgressInfo);

            Runnable target = () -> new CircleAreaPiCalculator(STEP, radius).calculate(taskProgressInfo);

            int taskId = this.repo.create(target);
            taskProgressInfo.setTaskName(String.format("Pi calculating task %d", taskId));
            return taskId;
        } catch (NumberFormatException ex) {
            throw new BadArgsException();
        }
    }
}
