package io.younsai.gestionprojetsagile.repository;

import io.younsai.gestionprojetsagile.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
