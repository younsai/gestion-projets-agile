package io.younsai.gestionprojetsagile.service;

import io.younsai.gestionprojetsagile.model.Epic;
import java.util.List;

public interface EpicService {
    Epic createEpic(Epic epic);

    // Update an existing Epic by id (merge title/description)
    Epic updateEpic(Long id, Epic epic);

    // Remove an epic (detach user stories and remove from any product backlog)
    void removeEpic(Long id);

    // List all epics
    List<Epic> listAll();

    // Read/get an epic by id
    Epic getEpic(Long id);

    // Search epics by titre or description
    List<Epic> search(String query);

    // Link a user story to an epic (ensure they belong to same backlog)
    io.younsai.gestionprojetsagile.model.UserStory linkUserStoryToEpic(Long epicId, Long userStoryId);
}
