package io.younsai.gestionprojetsagile.controller;

import io.younsai.gestionprojetsagile.dto.TaskDTO;
import io.younsai.gestionprojetsagile.mapper.TaskMapper;
import io.younsai.gestionprojetsagile.model.Task;
import io.younsai.gestionprojetsagile.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;
    private final TaskMapper taskMapper;

    public TaskController(TaskService taskService, TaskMapper taskMapper) {
        this.taskService = taskService;
        this.taskMapper = taskMapper;
    }

    @PostMapping
    public ResponseEntity<TaskDTO> create(@RequestBody TaskDTO dto) {
        try {
            Task t = taskMapper.toEntity(dto);
            Task saved = taskService.createTask(t);
            return ResponseEntity.status(201).body(taskMapper.toDto(saved));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<TaskDTO>> listAll() {
        List<Task> list = taskService.listAll();
        return ResponseEntity.ok(list.stream().map(taskMapper::toDto).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getById(@PathVariable Long id) {
        try {
            Task t = taskService.getTask(id);
            return ResponseEntity.ok(taskMapper.toDto(t));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDTO> update(@PathVariable Long id, @RequestBody TaskDTO dto) {
        try {
            Task t = taskMapper.toEntity(dto);
            Task updated = taskService.updateTask(id, t);
            return ResponseEntity.ok(taskMapper.toDto(updated));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            taskService.removeTask(id);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
