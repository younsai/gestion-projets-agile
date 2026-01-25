package io.younsai.gestionprojetsagile.dto;


import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class SprintBacklogDTO {
    private String nom;
    private List<UserStoryDTO> listeDesUserStories;
    private List<TaskDTO> listeDesTasks;
}
