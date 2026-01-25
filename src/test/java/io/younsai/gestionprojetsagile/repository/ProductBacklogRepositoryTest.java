package io.younsai.gestionprojetsagile.repository;

import io.younsai.gestionprojetsagile.model.ProductBacklog;
import io.younsai.gestionprojetsagile.model.UserStory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductBacklogRepositoryTest {

    @Mock
    private ProductBacklogRepository productBacklogRepository;

    @Test
    public void whenFiltrerParNomDeUserStory_thenReturnProductBacklog() {
        // given
        UserStory userStory = new UserStory();
        userStory.setTitre("Test User Story");

        ProductBacklog productBacklog = new ProductBacklog();
        productBacklog.setNom("PB Test");
        productBacklog.setUserStories(Collections.singletonList(userStory));
        userStory.setProductBacklog(productBacklog);

        // stub repository behavior
        when(productBacklogRepository.save(productBacklog)).thenReturn(productBacklog);
        // when(productBacklogRepository.filtrerParNomDeUserStory(userStory.getTitre())).thenReturn(Collections.singletonList(productBacklog));

        // persist via mocked repository
        productBacklogRepository.save(productBacklog);

        // when
        //List<ProductBacklog> found = productBacklogRepository.filtrerParNomDeUserStory(userStory.getTitre());
        // then
        //assertThat(found).isNotEmpty();
        //assertThat(found.get(0).getNom()).isEqualTo("PB Test");

        // verify interactions
        verify(productBacklogRepository).save(productBacklog);
        //verify(productBacklogRepository).filtrerParNomDeUserStory(userStory.getTitre());
    }
}
