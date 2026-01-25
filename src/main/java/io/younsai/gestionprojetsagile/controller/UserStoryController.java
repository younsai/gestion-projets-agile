package io.younsai.gestionprojetsagile.controller;

import io.younsai.gestionprojetsagile.dto.UserStoryDTO;
import io.younsai.gestionprojetsagile.mapper.UserStoryMapper;
import io.younsai.gestionprojetsagile.model.UserStory;
import io.younsai.gestionprojetsagile.service.UserStoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user-stories")
public class UserStoryController {

    private final UserStoryService userStoryService;
    private final UserStoryMapper userStoryMapper;

    public UserStoryController(UserStoryService userStoryService, UserStoryMapper userStoryMapper) {
        this.userStoryService = userStoryService;
        this.userStoryMapper = userStoryMapper;
    }

    @PostMapping
    public ResponseEntity<UserStoryDTO> create(@RequestParam Long productBacklogId, @RequestBody UserStoryDTO dto) {
        UserStory us = userStoryMapper.toEntity(dto);
        UserStory saved = userStoryService.createUserStory(productBacklogId, us);
        return ResponseEntity.status(201).body(userStoryMapper.toDto(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserStoryDTO> update(@PathVariable Long id, @RequestBody UserStoryDTO dto) {
        UserStory us = userStoryMapper.toEntity(dto);
        UserStory updated = userStoryService.updateUserStory(id, us);
        return ResponseEntity.ok(userStoryMapper.toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userStoryService.removeUserStory(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/link-epic/{epicId}")
    public ResponseEntity<UserStoryDTO> linkToEpic(@PathVariable Long id, @PathVariable Long epicId) {
        UserStory saved = userStoryService.linkUserStoryToEpic(id, epicId);
        return ResponseEntity.ok(userStoryMapper.toDto(saved));
    }
}
