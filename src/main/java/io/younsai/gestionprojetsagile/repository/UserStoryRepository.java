package io.younsai.gestionprojetsagile.repository;

import io.younsai.gestionprojetsagile.model.UserStory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserStoryRepository extends JpaRepository<UserStory, Long> {
}
