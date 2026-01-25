package io.younsai.gestionprojetsagile.service.impl;

import io.younsai.gestionprojetsagile.model.Task;
import io.younsai.gestionprojetsagile.model.UserStory;
import io.younsai.gestionprojetsagile.repository.TaskRepository;
import io.younsai.gestionprojetsagile.service.TaskService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository repository;

    public TaskServiceImpl(TaskRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public Task createTask(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("task must not be null");
        }
        return repository.save(task);
    }

    @Override
    public Task getTask(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("id must not be null");
        }
        return repository.findById(id)
                .orElseThrow(() -> new java.util.NoSuchElementException("Task not found: " + id));
    }

    @Override
    public List<Task> listAll(){
        return repository.findAll();
    }

    @Override
    @Transactional
    public Task updateTask(Long id, Task task) {
        if (id == null || task == null) {
            throw new IllegalArgumentException("id and task must not be null");
        }
        Task existing = repository.findById(id).orElseThrow(() -> new java.util.NoSuchElementException("Task not found: " + id));

        existing.setTitre(task.getTitre() != null ? task.getTitre() : existing.getTitre());
        existing.setDescription(task.getDescription() != null ? task.getDescription() : existing.getDescription());
        // statut is primitive int; we need a convention: use negative to indicate 'not provided' or always set
        existing.setStatut(task.getStatut());

        return repository.save(existing);
    }

    @Override
    @Transactional
    public void removeTask(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("id must not be null");
        }
        Task t = repository.findById(id).orElseThrow(() -> new java.util.NoSuchElementException("Task not found: " + id));

        UserStory us = t.getUserStory();
        if (us != null) {
            // Detach reference from Task side; UserStory has no listeDesTasks field in the model
            t.setUserStory(null);
        }

        repository.deleteById(id);
    }
}
