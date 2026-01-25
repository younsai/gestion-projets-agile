package io.younsai.gestionprojetsagile.service.impl;

import io.younsai.gestionprojetsagile.model.ProductBacklog;
import io.younsai.gestionprojetsagile.model.SprintBacklog;
import io.younsai.gestionprojetsagile.model.Task;
import io.younsai.gestionprojetsagile.model.UserStory;
import io.younsai.gestionprojetsagile.repository.SprintBacklogRepository;
import io.younsai.gestionprojetsagile.repository.TaskRepository;
import io.younsai.gestionprojetsagile.repository.UserStoryRepository;
import io.younsai.gestionprojetsagile.service.SprintBacklogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class SprintBacklogServiceImpl implements SprintBacklogService {

    private final SprintBacklogRepository repository;
    private final UserStoryRepository userStoryRepository;
    private final TaskRepository taskRepository;
    private final io.younsai.gestionprojetsagile.repository.ProductBacklogRepository productBacklogRepository;

    public SprintBacklogServiceImpl(SprintBacklogRepository repository, UserStoryRepository userStoryRepository, TaskRepository taskRepository, io.younsai.gestionprojetsagile.repository.ProductBacklogRepository productBacklogRepository) {
        this.repository = repository;
        this.userStoryRepository = userStoryRepository;
        this.taskRepository = taskRepository;
        this.productBacklogRepository = productBacklogRepository;
    }

    @Override
    @Transactional
    public SprintBacklog createSprintBacklog(SprintBacklog sprintBacklog) {
        if (sprintBacklog == null) {
            throw new IllegalArgumentException("sprintBacklog must not be null");
        }
        return repository.save(sprintBacklog);
    }

    @Override
    public List<SprintBacklog> listAll() {
        return repository.findAll();
    }

    @Override
    @Transactional
    public SprintBacklog updateSprintBacklog(Long id, SprintBacklog sprintBacklog) {
        if (id == null || sprintBacklog == null) {
            throw new IllegalArgumentException("id and sprintBacklog must not be null");
        }
        SprintBacklog existing = repository.findById(id).orElseThrow(() -> new NoSuchElementException("SprintBacklog not found: " + id));
        existing.setNom(sprintBacklog.getNom() != null ? sprintBacklog.getNom() : existing.getNom());
        // child lists (user stories, tasks) are managed via dedicated methods
        return repository.save(existing);
    }

    @Override
    public SprintBacklog getSprintBacklog(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("id must not be null");
        }
        return repository.findById(id).orElseThrow(() -> new NoSuchElementException("SprintBacklog not found: " + id));
    }

    @Override
    @Transactional
    public void removeSprintBacklog(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("id must not be null");
        }
        SprintBacklog sb = repository.findById(id).orElseThrow(() -> new NoSuchElementException("SprintBacklog not found: " + id));

        // Detach user stories from this sprint backlog
        if (sb.getListeDesUserStories() != null) {
            for (UserStory us : new ArrayList<>(sb.getListeDesUserStories())) {
                us.setSprintBacklog(null);
                if (us.getId() != null && userStoryRepository.existsById(us.getId())) {
                    userStoryRepository.save(us);
                }
            }
        }

        // Delete tasks associated to this sprint backlog
        if (sb.getListeDesTasks() != null) {
            for (Task t : new ArrayList<>(sb.getListeDesTasks())) {
                if (t.getId() != null && taskRepository.existsById(t.getId())) {
                    taskRepository.deleteById(t.getId());
                }
            }
        }

        // Finally delete the sprint backlog
        repository.deleteById(id);
    }

    @Override
    @Transactional
    public List<UserStory> addUserStoriesFromProductBacklog(Long sprintBacklogId, Long productBacklogId, List<Long> userStoryIds) {
        if (sprintBacklogId == null || productBacklogId == null || userStoryIds == null) {
            throw new IllegalArgumentException("sprintBacklogId, productBacklogId and userStoryIds must not be null");
        }

        SprintBacklog sprint = repository.findById(sprintBacklogId).orElseThrow(() -> new NoSuchElementException("SprintBacklog not found: " + sprintBacklogId));
        // verify product backlog exists
        productBacklogRepository.findById(productBacklogId).orElseThrow(() -> new NoSuchElementException("ProductBacklog not found: " + productBacklogId));

        if (sprint.getListeDesUserStories() == null) {
            sprint.setListeDesUserStories(new ArrayList<>());
        }

        List<UserStory> saved = new ArrayList<>();
        for (Long usId : userStoryIds) {
            UserStory us = userStoryRepository.findById(usId).orElseThrow(() -> new NoSuchElementException("UserStory not found: " + usId));
            // ensure the user story belongs to the given product backlog
            if (us.getProductBacklog() == null || !java.util.Objects.equals(us.getProductBacklog().getId(), productBacklogId)) {
                throw new IllegalArgumentException("UserStory " + usId + " does not belong to ProductBacklog " + productBacklogId);
            }

            // associate with sprint
            us.setSprintBacklog(sprint);
            UserStory savedUs = userStoryRepository.save(us);

            // add to sprint list if not already present
            if (sprint.getListeDesUserStories().stream().noneMatch(x -> java.util.Objects.equals(x.getId(), savedUs.getId()))) {
                sprint.getListeDesUserStories().add(savedUs);
            }
            saved.add(savedUs);
        }

        repository.save(sprint);
        return saved;
    }
}
