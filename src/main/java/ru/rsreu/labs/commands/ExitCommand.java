package ru.rsreu.labs.commands;

import ru.rsreu.labs.repo.TaskRepo;

public class ExitCommand extends TaskCommand {
    public ExitCommand(TaskRepo repo) {
        super(repo);
    }

    @Override
    public void execute() {
        repo.stopAll();
        System.out.println("Good buy!");
    }

    @Override
    public boolean needExit() {
        return true;
    }
}
