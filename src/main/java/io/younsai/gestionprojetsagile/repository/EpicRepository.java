package io.younsai.gestionprojetsagile.repository;

import io.younsai.gestionprojetsagile.model.Epic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EpicRepository extends JpaRepository<Epic, Long> {
    List<Epic> findByTitreIgnoreCaseContainingOrDescriptionIgnoreCaseContaining(String titreFragment, String descriptionFragment);
}
