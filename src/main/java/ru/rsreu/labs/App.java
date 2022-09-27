package ru.rsreu.labs;

import ru.rsreu.labs.commands.Command;
import ru.rsreu.labs.commands.EmptyCommand;
import ru.rsreu.labs.exceptions.TaskNotFoundException;
import ru.rsreu.labs.exceptions.WrongCommandException;
import ru.rsreu.labs.tasks.TaskCommandQualifier;
import ru.rsreu.labs.tasks.TaskCreator;
import ru.rsreu.labs.tasks.ThreadRepo;
import ru.rsreu.labs.tasks.commands.AwaitCommand;
import ru.rsreu.labs.tasks.pi.PiCalculatingTaskCreator;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class App {
    private final Logger logger = new Logger();
    private final InputStream inputStream = System.in;
    private final ThreadRepo repo = new ThreadRepo();
    private final TaskCreator taskCreator = new PiCalculatingTaskCreator(logger, repo);
    private final TaskCommandQualifier taskCommandQualifier = new TaskCommandQualifier(taskCreator);
    private final Scanner scanner = new Scanner(inputStream);

    public static void main(String[] args) throws IOException {
        new App().start();
    }

    private void start() throws IOException {
        logger.start();
        Command command = new EmptyCommand();
        printHelloText();
        do {
            String line = scanner.nextLine();
            try {
                command = taskCommandQualifier.qualify(line);
                try {
                    command.execute();
                    if (command instanceof AwaitCommand) clear(inputStream);
                } catch (TaskNotFoundException ex) {
                    System.out.println("Task not found");
                }
            } catch (WrongCommandException ex) {
                System.out.println("Wrong command");
            }
        } while (!command.needExit());
    }

    private void printHelloText() {
        System.out.println("Hello, you can calculate Pi using the area of a circle. For this you can use the commands:");
        System.out.println("start <circle radius>; stop <task id>; await <task id>; exit.");
    }

    private void clear(InputStream inputStream) throws IOException {
        inputStream.read(new byte[inputStream.available()]);
    }
}
