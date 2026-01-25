// java
package io.younsai.gestionprojetsagile.service.impl;

import io.younsai.gestionprojetsagile.model.Epic;
import io.younsai.gestionprojetsagile.model.ProductBacklog;
import io.younsai.gestionprojetsagile.model.UserStory;
import io.younsai.gestionprojetsagile.repository.EpicRepository;
import io.younsai.gestionprojetsagile.repository.ProductBacklogRepository;
import io.younsai.gestionprojetsagile.repository.UserStoryRepository;
import io.younsai.gestionprojetsagile.service.ProductBacklogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ProductBacklogServiceImpl implements ProductBacklogService {

    private final ProductBacklogRepository repository;
    private final UserStoryRepository userStoryRepository;
    private final EpicRepository epicRepository;

    public ProductBacklogServiceImpl(ProductBacklogRepository repository, UserStoryRepository userStoryRepository, EpicRepository epicRepository) {
        this.repository = repository;
        this.userStoryRepository = userStoryRepository;
        this.epicRepository = epicRepository;
    }

    @Override
    @Transactional
    public ProductBacklog createProductBacklog(ProductBacklog backlog) {
        if (backlog == null) {
            throw new IllegalArgumentException("backlog must not be null");
        }
        return repository.save(backlog);
    }

    @Override
    @Transactional
    public ProductBacklog updateProductBacklog(Long id, ProductBacklog backlog) {
        if (id == null || backlog == null) {
            throw new IllegalArgumentException("id and backlog must not be null");
        }
        ProductBacklog existing = repository.findById(id).orElseThrow(() -> new NoSuchElementException("ProductBacklog not found: " + id));
        existing.setNom(backlog.getNom() != null ? backlog.getNom() : existing.getNom());
        return repository.save(existing);
    }

    @Override
    @Transactional
    public void removeProductBacklog(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("id must not be null");
        }
        ProductBacklog existing = repository.findById(id).orElseThrow(() -> new NoSuchElementException("ProductBacklog not found: " + id));

        // Delete user stories linked to this backlog
        if (existing.getUserStories() != null) {
            for (UserStory us : new ArrayList<>(existing.getUserStories())) {
                if (us.getId() != null && userStoryRepository.existsById(us.getId())) {
                    userStoryRepository.deleteById(us.getId());
                }
            }
        }

        // Delete epics linked to this backlog
        if (existing.getListeDesEpics() != null) {
            for (Epic e : new ArrayList<>(existing.getListeDesEpics())) {
                if (e.getId() != null && epicRepository.existsById(e.getId())) {
                    epicRepository.deleteById(e.getId());
                }
            }
        }

        // Finally delete the backlog itself
        repository.deleteById(id);
    }


    @Override
    @Transactional
    public UserStory addUserStory(Long backlogId, UserStory userStory) {
        if (backlogId == null || userStory == null) {
            throw new IllegalArgumentException("backlogId and userStory must not be null");
        }
        ProductBacklog backlog = repository.findById(backlogId)
                .orElseThrow(() -> new NoSuchElementException("Backlog not found: " + backlogId));

        if (backlog.getUserStories() == null) {
            backlog.setUserStories(new ArrayList<>());
        }
        // ensure relationship
        userStory.setProductBacklog(backlog);
        // persist the user story explicitly (safer than relying on cascade)
        UserStory saved = userStoryRepository.save(userStory);
        backlog.getUserStories().add(saved);
        repository.save(backlog);
        return saved;
    }

    @Override
    @Transactional
    public UserStory updateUserStory(Long backlogId, UserStory userStory) {
        if (backlogId == null || userStory == null || userStory.getId() == null) {
            throw new IllegalArgumentException("backlogId and userStory (with id) must not be null");
        }
        ProductBacklog backlog = repository.findById(backlogId)
                .orElseThrow(() -> new NoSuchElementException("Backlog not found: " + backlogId));

        UserStory existing = userStoryRepository.findById(userStory.getId())
                .orElseThrow(() -> new NoSuchElementException("UserStory not found: " + userStory.getId()));

        // ensure the existing story belongs to the given backlog
        if (existing.getProductBacklog() == null || !Objects.equals(existing.getProductBacklog().getId(), backlogId)) {
            throw new IllegalArgumentException("UserStory does not belong to the specified backlog");
        }

        // merge fields
        existing.setTitre(userStory.getTitre() != null ? userStory.getTitre() : existing.getTitre());
        existing.setDescription(userStory.getDescription() != null ? userStory.getDescription() : existing.getDescription());
        existing.setPriorite(userStory.getPriorite() != null ? userStory.getPriorite() : existing.getPriorite());
        existing.setStatut(userStory.getStatut() != null ? userStory.getStatut() : existing.getStatut());

        UserStory saved = userStoryRepository.save(existing);
        // ensure backlog list reflects changes
        if (backlog.getUserStories() == null) {
            backlog.setUserStories(new ArrayList<>());
        }
        // replace in list if present
        for (int i = 0; i < backlog.getUserStories().size(); i++) {
            if (Objects.equals(backlog.getUserStories().get(i).getId(), saved.getId())) {
                backlog.getUserStories().set(i, saved);
                break;
            }
        }
        repository.save(backlog);
        return saved;
    }

    @Override
    @Transactional
    public void removeUserStory(Long backlogId, Long userStoryId) {
        if (backlogId == null || userStoryId == null) {
            throw new IllegalArgumentException("backlogId and userStoryId must not be null");
        }
        ProductBacklog backlog = repository.findById(backlogId)
                .orElseThrow(() -> new NoSuchElementException("Backlog not found: " + backlogId));
        List<UserStory> list = backlog.getUserStories();
        if (list != null) {
            boolean removed = list.removeIf(us -> Objects.equals(us.getId(), userStoryId));
            if (removed) {
                repository.save(backlog);
            }
        }
        // delete the user story entity
        if (userStoryRepository.existsById(userStoryId)) {
            userStoryRepository.deleteById(userStoryId);
        }
    }

    @Override
    public List<ProductBacklog> listAll() {
        return repository.findAll();
    }

    @Override
    public List<ProductBacklog> filterByName(String nameFragment) {
        if (nameFragment == null) {
            return listAll();
        }
        return repository.findByNomIgnoreCaseContaining(nameFragment);
    }

    @Override
    public List<ProductBacklog> sortByName(boolean ascending) {
        List<ProductBacklog> all = listAll();
        Comparator<ProductBacklog> cmp = Comparator.comparing(pb -> pb.getNom() == null ? "" : pb.getNom(), String.CASE_INSENSITIVE_ORDER);
        return all.stream().sorted(ascending ? cmp : cmp.reversed()).collect(Collectors.toList());
    }

    @Override
    public List<ProductBacklog> search(String query) {
        if (query == null || query.trim().isEmpty()) {
            return listAll();
        }
        return repository.searchByNameOrEpicTitle(query.trim());
    }

    @Override
    public ProductBacklog getProductBacklog(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("id must not be null");
        }
        return repository.findById(id).orElseThrow(() -> new NoSuchElementException("ProductBacklog not found: " + id));
    }

    @Override
    @Transactional
    public UserStory linkUserStoryToEpic(Long backlogId, Long userStoryId, Long epicId) {
        if (backlogId == null || userStoryId == null || epicId == null) {
            throw new IllegalArgumentException("backlogId, userStoryId, and epicId must not be null");
        }
        ProductBacklog backlog = findBacklogOrThrow(backlogId);
        UserStory userStory = findUserStoryOrThrow(userStoryId);
        Epic epic = findEpicOrThrow(epicId);

        // ensure the user story belongs to the backlog
        if (userStory.getProductBacklog() == null || !Objects.equals(userStory.getProductBacklog().getId(), backlogId)) {
            throw new IllegalArgumentException("UserStory must belong to the specified backlog");
        }

        // ensure the epic is part of the backlog's epic list
        boolean epicBelongsToBacklog = backlog.getListeDesEpics() != null && backlog.getListeDesEpics().stream()
                .anyMatch(e -> Objects.equals(e.getId(), epicId));
        if (!epicBelongsToBacklog) {
            throw new IllegalArgumentException("Epic does not belong to the specified backlog");
        }

        // set the epic on the user story (field is epicLie)
        userStory.setEpicLie(epic);
        UserStory saved = userStoryRepository.save(userStory);

        // keep epic's list in sync (optional)
        if (epic.getListeDesUserStories() == null) {
            epic.setListeDesUserStories(new ArrayList<>());
        }
        if (epic.getListeDesUserStories().stream().noneMatch(us -> Objects.equals(us.getId(), saved.getId()))) {
            epic.getListeDesUserStories().add(saved);
            epicRepository.save(epic);
        }

        return saved;
    }

    // --- helpers to centralize find-or-throw logic ---
    private ProductBacklog findBacklogOrThrow(Long id) {
        return repository.findById(id).orElseThrow(() -> new NoSuchElementException("ProductBacklog not found: " + id));
    }

    private UserStory findUserStoryOrThrow(Long id) {
        return userStoryRepository.findById(id).orElseThrow(() -> new NoSuchElementException("UserStory not found: " + id));
    }

    private Epic findEpicOrThrow(Long id) {
        return epicRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Epic not found: " + id));
    }

}
