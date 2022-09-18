package ru.rsreu.labs.commands;

public interface Command {
    void execute();

    default boolean needExit(){
        return false;
    }
}
