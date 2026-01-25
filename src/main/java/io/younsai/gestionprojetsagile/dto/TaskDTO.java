package io.younsai.gestionprojetsagile.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskDTO {
    private String titre;
    private String description;
    private int statut;
    private UserStoryDTO userStory;
}
