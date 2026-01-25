package io.younsai.gestionprojetsagile.service;

import io.younsai.gestionprojetsagile.model.UserStory;

public interface UserStoryService {
    // Create a new UserStory and attach it to a ProductBacklog
    UserStory createUserStory(Long productBacklogId, UserStory userStory);

    // Update an existing UserStory by id (merge safe fields)
    UserStory updateUserStory(Long id, UserStory userStory);

    // Remove a user story by id (detach from epic/sprint/productBacklog and delete)
    void removeUserStory(Long id);

    // Link a UserStory to an Epic (ensure they belong to the same ProductBacklog)
    UserStory linkUserStoryToEpic(Long userStoryId, Long epicId);
}
