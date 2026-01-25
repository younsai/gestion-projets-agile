package io.younsai.gestionprojetsagile.service;

import io.younsai.gestionprojetsagile.model.SprintBacklog;
import io.younsai.gestionprojetsagile.model.UserStory;

import java.util.List;

public interface SprintBacklogService {
    SprintBacklog createSprintBacklog(SprintBacklog sprintBacklog);

    // List all sprint backlogs
    List<SprintBacklog> listAll();

    // Update a sprint backlog (merge fields like name)
    SprintBacklog updateSprintBacklog(Long id, SprintBacklog sprintBacklog);

    // Remove a sprint backlog and detach/delete its children
    void removeSprintBacklog(Long id);

    // Read/get a sprint backlog by id
    SprintBacklog getSprintBacklog(Long id);

    // Select user stories from a product backlog and include them in a sprint backlog
    List<UserStory> addUserStoriesFromProductBacklog(Long sprintBacklogId, Long productBacklogId, List<Long> userStoryIds);
}
