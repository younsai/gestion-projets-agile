package io.younsai.gestionprojetsagile.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserStoryDTO {
    private String titre;
    private String description;
    private String priorite;
    private String statut;
    private EpicDTO epicLie;
    private SprintBacklogDTO sprintBacklog;
}
