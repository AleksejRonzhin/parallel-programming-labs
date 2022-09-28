package ru.rsreu.labs.tasks;

import ru.rsreu.labs.commands.Command;
import ru.rsreu.labs.exceptions.BadArgsException;
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
        String[] substrings = line.split(" ", 2);
        if (substrings.length == 0) {
            throw new WrongCommandException();
        }
        String commandEntry = substrings[0];
        if (commandEntry.equals("exit")) {
            return new ExitCommand(repo);
        }

        if (substrings.length < 2) {
            throw new WrongCommandException();
        }
        String[] parameters = substrings[1].split(" ");
        if (parameters.length == 0) {
            throw new WrongCommandException();
        }
        if (commandEntry.equals("start")) {
            try {
                int taskId = this.taskCreator.create(parameters);
                return new StartCommand(repo, taskId);
            } catch (BadArgsException ex){
                throw new WrongCommandException();
            }
        }
        if (commandEntry.equals("await") || commandEntry.equals("stop")) {
            try {
                int id = Integer.parseInt(parameters[0]);
                if (commandEntry.equals("await")) return new AwaitCommand(repo, id);
                return new StopCommand(repo, id);
            } catch (NumberFormatException ex) {
                throw new WrongCommandException();
            }
        }
        throw new WrongCommandException();
    }
}
