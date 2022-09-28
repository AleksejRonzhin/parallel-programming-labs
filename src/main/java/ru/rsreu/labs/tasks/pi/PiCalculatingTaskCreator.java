package ru.rsreu.labs.tasks.pi;

import ru.rsreu.labs.exceptions.BadArgsException;
import ru.rsreu.labs.integrals.CircleAreaPiCalculator;
import ru.rsreu.labs.tasks.TaskCreator;
import ru.rsreu.labs.tasks.ThreadRepo;

public class PiCalculatingTaskCreator extends TaskCreator {
    private static final double STEP = 1E-9;

    public PiCalculatingTaskCreator(ThreadRepo repo) {
        super(repo);
    }

    @Override
    public int create(String[] args) throws BadArgsException {
        try {
            double radius = Double.parseDouble(args[0]);
            Runnable target = () -> {
                double pi = new CircleAreaPiCalculator(STEP, radius).calculate();
                System.out.printf("Result pi calculating task: %s\n", pi);
            };
            return this.repo.create(target);
        } catch (NumberFormatException ex) {
            throw new BadArgsException();
        }
    }
}