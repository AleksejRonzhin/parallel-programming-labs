package ru.rsreu.labs.commands;

import ru.rsreu.labs.TargetCreator;
import ru.rsreu.labs.repo.ThreadRepo;

public class CommandQualifier {
    private final ThreadRepo repo = new ThreadRepo();
    private final TargetCreator targetCreator;

    public CommandQualifier(TargetCreator targetCreator) {
        this.targetCreator = targetCreator;
    }

    public Command qualify(String line) {
        try {
            String[] substrings = line.split(" ", 2);
            String commandEntry = substrings[0];
            if (commandEntry.equals("exit")) {
                return new ExitCommand(repo);
            }
            String[] parameters = substrings[1].split(" ");
            if (commandEntry.equals("start")) {
                Runnable target = this.targetCreator.create(parameters);
                return new StartCommand(repo, target);
            }
            if (commandEntry.equals("await") || commandEntry.equals("stop")) {
                int id = Integer.parseInt(parameters[0]);
                if (commandEntry.equals("await")) return new AwaitCommand(repo, id);
                return new StopCommand(repo, id);
            }
            return new EmptyCommand();
        } catch (Exception ex) {
            return new EmptyCommand();
        }
    }
}
