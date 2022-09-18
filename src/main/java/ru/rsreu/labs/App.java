package ru.rsreu.labs;

import ru.rsreu.labs.commands.Command;
import ru.rsreu.labs.commands.CommandQualifier;

import java.util.Scanner;

public class App {


    public static void main(String[] args) {
        new App().start();
    }

    private void start(){
        TargetCreator targetCreator = new PiCalculatingCreator();
        Scanner scanner = new Scanner(System.in);
        CommandQualifier commandQualifier = new CommandQualifier(targetCreator);
        Command command;
        do{
            System.out.print("Input: ");
            String line = scanner.nextLine();

            command = commandQualifier.qualify(line);
            command.execute();
            scanner.reset();
        }while(!command.needExit()); // ACTIVE, EXIT, AWAIT
    }
}
