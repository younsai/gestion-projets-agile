package io.younsai.gestionprojetsagile.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class SprintBacklog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "sprintBacklog")
    private List<UserStory> listeDesUserStories = new ArrayList<>();

    @OneToMany
    @JoinColumn(name = "sprint_backlog_id")
    private List<Task> listeDesTasks = new ArrayList<>();

}
