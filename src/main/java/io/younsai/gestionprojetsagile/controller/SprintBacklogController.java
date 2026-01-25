package io.younsai.gestionprojetsagile.controller;

import io.younsai.gestionprojetsagile.dto.SprintBacklogDTO;
import io.younsai.gestionprojetsagile.dto.UserStoryDTO;
import io.younsai.gestionprojetsagile.mapper.SprintBacklogMapper;
import io.younsai.gestionprojetsagile.mapper.UserStoryMapper;
import io.younsai.gestionprojetsagile.model.SprintBacklog;
import io.younsai.gestionprojetsagile.model.UserStory;
import io.younsai.gestionprojetsagile.service.SprintBacklogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/sprints")
public class SprintBacklogController {

    private final SprintBacklogService sprintBacklogService;
    private final SprintBacklogMapper sprintBacklogMapper;
    private final UserStoryMapper userStoryMapper;

    public SprintBacklogController(SprintBacklogService sprintBacklogService, SprintBacklogMapper sprintBacklogMapper, UserStoryMapper userStoryMapper) {
        this.sprintBacklogService = sprintBacklogService;
        this.sprintBacklogMapper = sprintBacklogMapper;
        this.userStoryMapper = userStoryMapper;
    }

    @PostMapping
    public ResponseEntity<SprintBacklogDTO> create(@RequestBody SprintBacklogDTO dto) {
        try {
            SprintBacklog sb = sprintBacklogMapper.toEntity(dto);
            SprintBacklog saved = sprintBacklogService.createSprintBacklog(sb);
            return ResponseEntity.status(201).body(sprintBacklogMapper.toDto(saved));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<SprintBacklogDTO>> listAll() {
        List<SprintBacklog> all = sprintBacklogService.listAll();
        return ResponseEntity.ok(all.stream().map(sprintBacklogMapper::toDto).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SprintBacklogDTO> getById(@PathVariable Long id) {
        try {
            SprintBacklog sb = sprintBacklogService.getSprintBacklog(id);
            return ResponseEntity.ok(sprintBacklogMapper.toDto(sb));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<SprintBacklogDTO> update(@PathVariable Long id, @RequestBody SprintBacklogDTO dto) {
        try {
            SprintBacklog sb = sprintBacklogMapper.toEntity(dto);
            SprintBacklog updated = sprintBacklogService.updateSprintBacklog(id, sb);
            return ResponseEntity.ok(sprintBacklogMapper.toDto(updated));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            sprintBacklogService.removeSprintBacklog(id);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{sprintId}/import-from-product-backlog/{productBacklogId}")
    public ResponseEntity<List<UserStoryDTO>> importFromProductBacklog(@PathVariable Long sprintId, @PathVariable Long productBacklogId, @RequestBody List<Long> userStoryIds) {
        try {
            List<UserStory> moved = sprintBacklogService.addUserStoriesFromProductBacklog(sprintId, productBacklogId, userStoryIds);
            return ResponseEntity.ok(moved.stream().map(userStoryMapper::toDto).collect(Collectors.toList()));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
