package ru.rsreu.labs.tasks.pi;

import ru.rsreu.labs.exceptions.BadArgsException;
import ru.rsreu.labs.integrals.CircleAreaPiCalculator;
import ru.rsreu.labs.tasks.Task;
import ru.rsreu.labs.tasks.TaskCreator;
import ru.rsreu.labs.tasks.TaskRepository;
import ru.rsreu.labs.tasks.progress.TaskProgress;
import ru.rsreu.labs.tasks.progress.TaskProgressLogger;

public class PiCalculatingTaskCreator extends TaskCreator {
    private static final double STEP = 1E-9;
    private final TaskProgressLogger<Double> taskProgressLogger = new TaskProgressLogger<>();

    public PiCalculatingTaskCreator(TaskRepository repo) {
        super(repo);
    }

    @Override
    public int create(String[] args) throws BadArgsException {
        try {
            double radius = Double.parseDouble(args[0]);

            Task task = this.repo.create("Pi calculating task ");
            TaskProgress<Double> taskProgress = new TaskProgress<>();
            taskProgressLogger.logProgress(taskProgress, task.getName());

            Runnable target = () -> {
                long start = System.currentTimeMillis();
                try {

                    double pi = new CircleAreaPiCalculator(STEP, radius).calculate(taskProgress);
                    long end = System.currentTimeMillis();
                    long time = end - start;
                    System.out.printf("%s finished. Result: %s. Time: %d ms.\n", task.getName(), pi, time);
                } catch (InterruptedException ex) {
                    System.out.printf("%s interrupted\n", task.getName());
                }
            };

            task.setTarget(target);

            return task.getId();
        } catch (NumberFormatException | IndexOutOfBoundsException ex) {
            throw new BadArgsException();
        }
    }
}
