package ru.rsreu.labs.commands;

import ru.rsreu.labs.exceptions.TaskNotFoundException;

public interface Command {
    void execute() throws TaskNotFoundException;

    default boolean needExit(){
        return false;
    }
}
