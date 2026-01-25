package io.younsai.gestionprojetsagile.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class EpicDTO {
    private String titre;
    private String description;

    private List<UserStoryDTO> listeDesUserStories;
}
