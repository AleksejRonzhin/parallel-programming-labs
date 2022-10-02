package ru.rsreu.labs.tasks;

import ru.rsreu.labs.exceptions.TaskIsOverException;
import ru.rsreu.labs.exceptions.TaskNotFoundException;

import java.util.ArrayList;
import java.util.Collection;

public class TaskRepository {
    private final Collection<Task> tasks = new ArrayList<>();

    public Task create(String namePrefix) {
        int id = tasks.size();
        String name = namePrefix + id;
        Task task = new Task(id);
        task.setName(name);
        tasks.add(task);
        return task;
    }

    public void start(int id) throws TaskNotFoundException {
        Task task = getByTaskId(id);
        task.start();
    }

    public void stop(int id) throws TaskNotFoundException, TaskIsOverException {
        Task task = getNotFinishedTaskById(id);
        task.stop();
    }

    public void stopAll() {
        tasks.forEach(Task::stop);
    }

    public void await(int id) throws TaskNotFoundException, TaskIsOverException, InterruptedException {
        Task task = getNotFinishedTaskById(id);
        task.await();
    }

    private Task getByTaskId(int id) throws TaskNotFoundException {
        for(Task task: tasks){
            if(task.getId() == id){
                return task;
            }
        }
        throw new TaskNotFoundException();
    }

    private Task getNotFinishedTaskById(int taskId) throws TaskNotFoundException, TaskIsOverException {
        Task task = getByTaskId(taskId);
        if (task.isFinished()) throw new TaskIsOverException();
        return task;
    }
}