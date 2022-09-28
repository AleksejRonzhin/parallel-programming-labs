package ru.rsreu.labs.tasks.progress;

import java.util.EventObject;

public class TaskProgressInfoEvent extends EventObject {
    private final String message;

    public TaskProgressInfoEvent(Object source, String message) {
        super(source);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
