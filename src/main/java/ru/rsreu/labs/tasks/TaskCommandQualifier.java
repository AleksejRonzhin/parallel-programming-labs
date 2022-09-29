package ru.rsreu.labs.tasks;

import ru.rsreu.labs.commands.Command;
import ru.rsreu.labs.exceptions.BadArgsException;
import ru.rsreu.labs.exceptions.ParameterNotFoundException;
import ru.rsreu.labs.exceptions.UnknownCommandException;
import ru.rsreu.labs.exceptions.WrongCommandException;
import ru.rsreu.labs.tasks.commands.AwaitCommand;
import ru.rsreu.labs.tasks.commands.ExitCommand;
import ru.rsreu.labs.tasks.commands.StartCommand;
import ru.rsreu.labs.tasks.commands.StopCommand;

import java.util.Arrays;

public class TaskCommandQualifier {
    private final TaskRepository repo;
    private final TaskCreator taskCreator;

    public TaskCommandQualifier(TaskCreator taskCreator) {
        this.taskCreator = taskCreator;
        this.repo = taskCreator.getRepo();
    }

    public Command qualify(String line) throws WrongCommandException, UnknownCommandException {
        try {
            String commandEntry = getParameter(line, 0);
            if (commandEntry.equals("exit")) return new ExitCommand(repo);

            if (commandEntry.equals("start")) {
                    String[] parameters = getParameters(line, 1);
                    int taskId = this.taskCreator.create(parameters);
                    return new StartCommand(repo, taskId);
            }

            if (commandEntry.equals("await") || commandEntry.equals("stop")) {
                int id = Integer.parseInt(getParameter(line, 1));
                if (commandEntry.equals("await")) return new AwaitCommand(repo, id);
                return new StopCommand(repo, id);
            }

            throw new UnknownCommandException();
        } catch(ParameterNotFoundException | BadArgsException | NumberFormatException ex){
            throw new WrongCommandException();
        }
    }

    private String getParameter(String line, int number) throws ParameterNotFoundException {
        try {
            return line.split(" ")[number];
        } catch (IndexOutOfBoundsException ex){
            throw new ParameterNotFoundException();
        }
    }

    private String[] getParameters(String line, int startIndex){
        String[] parameters = line.split(" ");
        return Arrays.copyOfRange(parameters, startIndex, parameters.length);
    }
}