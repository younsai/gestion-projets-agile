package io.younsai.gestionprojetsagile.model;

import io.younsai.gestionprojetsagile.common.contracts.UserStoryCrud;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserStory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titre;
    private String description;
    private String priorite;
    private String statut;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "epic_id")
    private Epic epicLie;

    @ManyToOne
    @JoinColumn(name = "product_backlog_id", nullable = false)
    private ProductBacklog productBacklog;

    @ManyToOne
    @JoinColumn(name = "sprint_backlog_id")
    private SprintBacklog sprintBacklog;
}



