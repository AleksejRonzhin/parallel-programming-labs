package ru.rsreu.labs.commands;

import ru.rsreu.labs.commands.ThreadCommand;
import ru.rsreu.labs.repo.ThreadRepo;

public class ExitCommand extends ThreadCommand {
    public ExitCommand(ThreadRepo repo) {
        super(repo);
    }

    @Override
    public void execute() {
        System.out.println("Good buy");
    }

    @Override
    public boolean needExit() {
        return true;
    }
}
