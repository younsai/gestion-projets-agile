package io.younsai.gestionprojetsagile.service;

import io.younsai.gestionprojetsagile.model.Task;

import java.util.List;

public interface TaskService {
    Task createTask(Task task);

    // Read/get a task by id
    Task getTask(Long id);

    // List all tasks
    List<Task> listAll();

    // Update an existing task (merge fields)
    Task updateTask(Long id, Task task);

    // Remove a task by id (detach from its UserStory and delete)
    void removeTask(Long id);
}
