package io.younsai.gestionprojetsagile.service.impl;

import io.younsai.gestionprojetsagile.model.Epic;
import io.younsai.gestionprojetsagile.model.ProductBacklog;
import io.younsai.gestionprojetsagile.model.UserStory;
import io.younsai.gestionprojetsagile.repository.EpicRepository;
import io.younsai.gestionprojetsagile.repository.ProductBacklogRepository;
import io.younsai.gestionprojetsagile.repository.UserStoryRepository;
import io.younsai.gestionprojetsagile.service.EpicService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class EpicServiceImpl implements EpicService {

    private final EpicRepository repository;
    private final UserStoryRepository userStoryRepository;
    private final ProductBacklogRepository productBacklogRepository;

    public EpicServiceImpl(EpicRepository repository, UserStoryRepository userStoryRepository, ProductBacklogRepository productBacklogRepository) {
        this.repository = repository;
        this.userStoryRepository = userStoryRepository;
        this.productBacklogRepository = productBacklogRepository;
    }

    @Override
    @Transactional
    public Epic createEpic(Epic epic) {
        if (epic == null) {
            throw new IllegalArgumentException("epic must not be null");
        }
        return repository.save(epic);
    }

    @Override
    @Transactional
    public Epic updateEpic(Long id, Epic epic) {
        if (id == null || epic == null) {
            throw new IllegalArgumentException("id and epic must not be null");
        }
        Epic existing = findEpicOrThrow(id);
        existing.setTitre(epic.getTitre() != null ? epic.getTitre() : existing.getTitre());
        existing.setDescription(epic.getDescription() != null ? epic.getDescription() : existing.getDescription());
        // Do not replace child user stories here; manage them via dedicated methods
        return repository.save(existing);
    }

    @Override
    @Transactional
    public void removeEpic(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("id must not be null");
        }
        Epic epic = findEpicOrThrow(id);

        // Detach user stories: set epicLie = null and save
        if (epic.getListeDesUserStories() != null) {
            for (UserStory us : new ArrayList<>(epic.getListeDesUserStories())) {
                us.setEpicLie(null);
                if (us.getId() != null && userStoryRepository.existsById(us.getId())) {
                    userStoryRepository.save(us);
                }
            }
        }

        // Remove epic from its product backlog (if any)
        for (ProductBacklog pb : productBacklogRepository.findAll()) {
            if (pb.getListeDesEpics() != null) {
                boolean removed = pb.getListeDesEpics().removeIf(e -> Objects.equals(e.getId(), id));
                if (removed) {
                    productBacklogRepository.save(pb);
                }
            }
        }

        repository.deleteById(id);
    }

    @Override
    public List<Epic> listAll() {
        return repository.findAll();
    }

    @Override
    public Epic getEpic(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("id must not be null");
        }
        return findEpicOrThrow(id);
    }

    @Override
    public List<Epic> search(String query) {
        if (query == null || query.trim().isEmpty()) {
            return listAll();
        }
        String q = query.trim();
        return repository.findByTitreIgnoreCaseContainingOrDescriptionIgnoreCaseContaining(q, q);
    }

    @Override
    @Transactional
    public UserStory linkUserStoryToEpic(Long epicId, Long userStoryId) {
        if (epicId == null || userStoryId == null) {
            throw new IllegalArgumentException("epicId and userStoryId must not be null");
        }
        Epic epic = findEpicOrThrow(epicId);
        UserStory userStory = findUserStoryOrThrow(userStoryId);

        // Find the product backlog that contains this epic
        Optional<ProductBacklog> maybePb = productBacklogRepository.findByListeDesEpics_Id(epicId);
        if (maybePb.isEmpty()) {
            throw new IllegalStateException("Epic is not attached to any ProductBacklog");
        }
        ProductBacklog pb = maybePb.get();

        // Ensure user story belongs to the same backlog
        if (userStory.getProductBacklog() == null || !java.util.Objects.equals(userStory.getProductBacklog().getId(), pb.getId())) {
            throw new IllegalArgumentException("UserStory must belong to the same ProductBacklog as the Epic");
        }

        // Link and persist
        userStory.setEpicLie(epic);
        io.younsai.gestionprojetsagile.model.UserStory saved = userStoryRepository.save(userStory);

        // keep epic's list in sync
        if (epic.getListeDesUserStories() == null) {
            epic.setListeDesUserStories(new ArrayList<>());
        }
        if (epic.getListeDesUserStories().stream().noneMatch(us -> java.util.Objects.equals(us.getId(), saved.getId()))) {
            epic.getListeDesUserStories().add(saved);
            repository.save(epic);
        }

        return saved;
    }

    // helpers
    private Epic findEpicOrThrow(Long id) {
        return repository.findById(id).orElseThrow(() -> new NoSuchElementException("Epic not found: " + id));
    }

    private UserStory findUserStoryOrThrow(Long id) {
        return userStoryRepository.findById(id).orElseThrow(() -> new NoSuchElementException("UserStory not found: " + id));
    }

}
