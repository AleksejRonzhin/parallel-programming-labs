package ru.rsreu.labs.tasks.commands;

import ru.rsreu.labs.tasks.ThreadRepo;

public class ExitCommand extends ThreadCommand {
    public ExitCommand(ThreadRepo repo) {
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
