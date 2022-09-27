package ru.rsreu.labs.tasks;

import ru.rsreu.labs.commands.Command;
import ru.rsreu.labs.exceptions.WrongCommandException;
import ru.rsreu.labs.tasks.commands.AwaitCommand;
import ru.rsreu.labs.tasks.commands.ExitCommand;
import ru.rsreu.labs.tasks.commands.StartCommand;
import ru.rsreu.labs.tasks.commands.StopCommand;

public class TaskCommandQualifier {
    private final ThreadRepo repo;
    private final TaskCreator taskCreator;

    public TaskCommandQualifier(TaskCreator taskCreator) {
        this.taskCreator = taskCreator;
        this.repo = taskCreator.getRepo();
    }

    public Command qualify(String line) throws WrongCommandException {
        try {
            String[] substrings = line.split(" ", 2);
            String commandEntry = substrings[0];
            if (commandEntry.equals("exit")) {
                return new ExitCommand(repo);
            }
            String[] parameters = substrings[1].split(" ");
            if (commandEntry.equals("start")) {
                int taskId = this.taskCreator.create(parameters);
                return new StartCommand(repo, taskId);
            }
            if (commandEntry.equals("await") || commandEntry.equals("stop")) {
                int id = Integer.parseInt(parameters[0]);
                if (commandEntry.equals("await")) return new AwaitCommand(repo, id);
                return new StopCommand(repo, id);
            }
            throw new WrongCommandException();
        } catch (Exception ignore) {
            throw new WrongCommandException();
        }
    }
}
