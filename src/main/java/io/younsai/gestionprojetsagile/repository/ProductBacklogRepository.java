package io.younsai.gestionprojetsagile.repository;

import io.younsai.gestionprojetsagile.model.ProductBacklog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductBacklogRepository extends JpaRepository<ProductBacklog, Long> {
    public List<ProductBacklog> existsByNom(String nom);

    // Find by name containing fragment (case-insensitive)
    List<ProductBacklog> findByNomIgnoreCaseContaining(String fragment);

    // Search by product backlog name or by epic title
    @Query("select distinct p from ProductBacklog p left join p.listeDesEpics e where lower(p.nom) like lower(concat('%', :q, '%')) or lower(e.titre) like lower(concat('%', :q, '%'))")
    List<ProductBacklog> searchByNameOrEpicTitle(@Param("q") String query);

    // Find the product backlog that contains an epic with the given id
    Optional<ProductBacklog> findByListeDesEpics_Id(Long epicId);
}
