package ru.rsreu.labs.tasks.progress;

public class TaskProgressLogger<T> {
    public void logProgress(TaskProgressInfo<T> task) {
        task.setListener(new LoggerListener());
    }

    private class LoggerListener implements TaskProgressInfoEventListener<T>{
        @Override
        public void taskProgressInfo(TaskProgressEvent<T> event) {
            System.out.println(event.getProgress());
        }

        @Override
        public void taskCompleted(TaskCompletedEvent<T> event) {
            System.out.println("Logger " + event.getResult());
        }
    }
}