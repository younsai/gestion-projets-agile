package io.younsai.gestionprojetsagile.dto;

import lombok.*;


import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductBacklogDTO {
    private String nom;
    private List<EpicDTO> listeDesEpics;
}
