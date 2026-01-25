package io.younsai.gestionprojetsagile.service.impl;

import io.younsai.gestionprojetsagile.model.Epic;
import io.younsai.gestionprojetsagile.model.ProductBacklog;
import io.younsai.gestionprojetsagile.model.SprintBacklog;
import io.younsai.gestionprojetsagile.model.UserStory;
import io.younsai.gestionprojetsagile.repository.EpicRepository;
import io.younsai.gestionprojetsagile.repository.ProductBacklogRepository;
import io.younsai.gestionprojetsagile.repository.SprintBacklogRepository;
import io.younsai.gestionprojetsagile.repository.UserStoryRepository;
import io.younsai.gestionprojetsagile.service.UserStoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserStoryServiceImpl implements UserStoryService {

    private final ProductBacklogRepository productBacklogRepository;
    private final UserStoryRepository userStoryRepository;
    private final EpicRepository epicRepository;
    private final SprintBacklogRepository sprintBacklogRepository;

    public UserStoryServiceImpl(ProductBacklogRepository productBacklogRepository, UserStoryRepository userStoryRepository, EpicRepository epicRepository, SprintBacklogRepository sprintBacklogRepository) {
        this.productBacklogRepository = productBacklogRepository;
        this.userStoryRepository = userStoryRepository;
        this.epicRepository = epicRepository;
        this.sprintBacklogRepository = sprintBacklogRepository;
    }

    @Override
    @Transactional
    public UserStory createUserStory(Long productBacklogId, UserStory userStory) {
        if (productBacklogId == null || userStory == null) {
            throw new IllegalArgumentException("productBacklogId and userStory must not be null");
        }
        ProductBacklog pb = productBacklogRepository.findById(productBacklogId)
                .orElseThrow(() -> new NoSuchElementException("ProductBacklog not found: " + productBacklogId));

        if (pb.getUserStories() == null) {
            pb.setUserStories(new ArrayList<>());
        }

        userStory.setProductBacklog(pb);
        UserStory saved = userStoryRepository.save(userStory);
        pb.getUserStories().add(saved);
        productBacklogRepository.save(pb);
        return saved;
    }

    @Override
    @Transactional
    public UserStory updateUserStory(Long id, UserStory userStory) {
        if (id == null || userStory == null) {
            throw new IllegalArgumentException("id and userStory must not be null");
        }
        UserStory existing = userStoryRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("UserStory not found: " + id));

        // Merge safe fields
        existing.setTitre(userStory.getTitre() != null ? userStory.getTitre() : existing.getTitre());
        existing.setDescription(userStory.getDescription() != null ? userStory.getDescription() : existing.getDescription());
        existing.setPriorite(userStory.getPriorite() != null ? userStory.getPriorite() : existing.getPriorite());
        existing.setStatut(userStory.getStatut() != null ? userStory.getStatut() : existing.getStatut());

        return userStoryRepository.save(existing);
    }

    @Override
    @Transactional
    public void removeUserStory(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("id must not be null");
        }
        UserStory us = userStoryRepository.findById(id).orElseThrow(() -> new NoSuchElementException("UserStory not found: " + id));

        // Detach from Epic
        Epic epic = us.getEpicLie();
        if (epic != null) {
            if (epic.getListeDesUserStories() != null) {
                epic.getListeDesUserStories().removeIf(u -> Objects.equals(u.getId(), id));
                epicRepository.save(epic);
            }
            us.setEpicLie(null);
        }

        // Detach from SprintBacklog
        SprintBacklog sprint = us.getSprintBacklog();
        if (sprint != null) {
            if (sprint.getListeDesUserStories() != null) {
                sprint.getListeDesUserStories().removeIf(u -> Objects.equals(u.getId(), id));
                sprintBacklogRepository.save(sprint);
            }
            us.setSprintBacklog(null);
        }

        // Detach from ProductBacklog
        ProductBacklog pb = us.getProductBacklog();
        if (pb != null) {
            if (pb.getUserStories() != null) {
                pb.getUserStories().removeIf(u -> Objects.equals(u.getId(), id));
                productBacklogRepository.save(pb);
            }
            us.setProductBacklog(null);
        }

        // Finally delete the user story
        userStoryRepository.deleteById(id);
    }

    @Override
    @Transactional
    public UserStory linkUserStoryToEpic(Long userStoryId, Long epicId) {
        if (userStoryId == null || epicId == null) {
            throw new IllegalArgumentException("userStoryId and epicId must not be null");
        }
        UserStory us = userStoryRepository.findById(userStoryId).orElseThrow(() -> new NoSuchElementException("UserStory not found: " + userStoryId));
        Epic epic = epicRepository.findById(epicId).orElseThrow(() -> new NoSuchElementException("Epic not found: " + epicId));

        // ensure the user story belongs to a product backlog
        if (us.getProductBacklog() == null) {
            throw new IllegalArgumentException("UserStory does not belong to any ProductBacklog");
        }

        // If epic has associated user stories we can infer productBacklog, otherwise accept link if epic exists in same backlog's list
        boolean epicBelongsToSamePb = false;
        if (epic.getListeDesUserStories() != null && !epic.getListeDesUserStories().isEmpty()) {
            // infer productBacklog from first user story of epic
            UserStory first = epic.getListeDesUserStories().get(0);
            if (first != null && first.getProductBacklog() != null && Objects.equals(first.getProductBacklog().getId(), us.getProductBacklog().getId())) {
                epicBelongsToSamePb = true;
            }
        }

        if (!epicBelongsToSamePb) {
            // fallback: check if the product backlog's epic list contains this epic
            Optional<ProductBacklog> maybePb = productBacklogRepository.findByListeDesEpics_Id(epicId);
            if (maybePb.isPresent() && us.getProductBacklog() != null && Objects.equals(maybePb.get().getId(), us.getProductBacklog().getId())) {
                epicBelongsToSamePb = true;
            }
        }

        if (!epicBelongsToSamePb) {
            throw new IllegalArgumentException("Epic does not belong to the same ProductBacklog as the UserStory");
        }

        // set epic on user story and save
        us.setEpicLie(epic);
        UserStory saved = userStoryRepository.save(us);

        // keep epic's list in sync
        if (epic.getListeDesUserStories() == null) {
            epic.setListeDesUserStories(new ArrayList<>());
        }
        if (epic.getListeDesUserStories().stream().noneMatch(u -> Objects.equals(u.getId(), saved.getId()))) {
            epic.getListeDesUserStories().add(saved);
            epicRepository.save(epic);
        }

        return saved;
    }
}
