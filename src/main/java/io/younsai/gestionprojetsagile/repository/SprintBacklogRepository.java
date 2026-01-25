package io.younsai.gestionprojetsagile.repository;

import io.younsai.gestionprojetsagile.model.SprintBacklog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SprintBacklogRepository extends JpaRepository<SprintBacklog, Long> {
}
