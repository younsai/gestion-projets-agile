// java
package io.younsai.gestionprojetsagile.service;

import io.younsai.gestionprojetsagile.model.ProductBacklog;
import io.younsai.gestionprojetsagile.model.UserStory;

import java.util.List;

public interface ProductBacklogService {

    // Create a new ProductBacklog
    ProductBacklog createProductBacklog(ProductBacklog backlog);

    // Update an existing ProductBacklog
    ProductBacklog updateProductBacklog(Long id, ProductBacklog backlog);

    // Remove a product backlog (and its associated user stories/epics)
    void removeProductBacklog(Long id);

    // UserStory management within a ProductBacklog
    UserStory addUserStory(Long backlogId, UserStory userStory);

    UserStory updateUserStory(Long backlogId, UserStory userStory);

    void removeUserStory(Long backlogId, Long userStoryId);

    // Link a UserStory to an Epic
    UserStory linkUserStoryToEpic(Long backlogId, Long userStoryId, Long epicId);

    // Read/get a product backlog by id
    ProductBacklog getProductBacklog(Long id);

    // Listing all product backlogs
    List<ProductBacklog> listAll();

    // Filter product backlogs by name containing a fragment (case-insensitive)
    List<ProductBacklog> filterByName(String nameFragment);

    // Sort product backlogs by name (ascending if true, descending if false)
    List<ProductBacklog> sortByName(boolean ascending);

    // Search product backlogs by name or epic title containing the query (case-insensitive)
    List<ProductBacklog> search(String query);

}