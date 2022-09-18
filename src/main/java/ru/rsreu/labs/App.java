package ru.rsreu.labs;

import ru.rsreu.labs.commands.AwaitCommand;
import ru.rsreu.labs.commands.Command;
import ru.rsreu.labs.commands.CommandQualifier;
import ru.rsreu.labs.commands.EmptyCommand;
import ru.rsreu.labs.exceptions.TaskNotFoundException;
import ru.rsreu.labs.exceptions.WrongCommandException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class App {
    private final InputStream inputStream = System.in;
    private final TargetCreator targetCreator = new PiCalculatingCreator();
    private final Scanner scanner = new Scanner(inputStream);
    private final CommandQualifier commandQualifier = new CommandQualifier(targetCreator);

    public static void main(String[] args) throws IOException {
        new App().start();
    }

    private void start() throws IOException {
        Command command = new EmptyCommand();
        printHelloText();
        do {
            String line = scanner.nextLine();
            try {
                command = commandQualifier.qualify(line);
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
