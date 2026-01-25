package io.younsai.gestionprojetsagile.service.impl;

import io.younsai.gestionprojetsagile.model.ProductBacklog;
import io.younsai.gestionprojetsagile.repository.EpicRepository;
import io.younsai.gestionprojetsagile.repository.ProductBacklogRepository;
import io.younsai.gestionprojetsagile.repository.UserStoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductBacklogServiceImplTest {

    @Mock
    private ProductBacklogRepository repository;
    @Mock
    private UserStoryRepository userStoryRepository;
    @Mock
    private EpicRepository epicRepository;

    @InjectMocks
    private ProductBacklogServiceImpl service;

    private ProductBacklog pb;

    @BeforeEach
    void setUp() {
        pb = ProductBacklog.builder().nom("PB1").build();
        pb.setId(1L);
        pb.setUserStories(new ArrayList<>());
        pb.setListeDesEpics(new ArrayList<>());
    }

    @Test
    void createProductBacklog_shouldSaveAndReturn() {
        when(repository.save(any())).thenReturn(pb);

        ProductBacklog res = service.createProductBacklog(new ProductBacklog());

        assertThat(res).isNotNull();
        assertThat(res.getNom()).isEqualTo("PB1");
        verify(repository).save(any());
    }

    @Test
    void getProductBacklog_found() {
        when(repository.findById(1L)).thenReturn(Optional.of(pb));

        ProductBacklog res = service.getProductBacklog(1L);

        assertThat(res).isNotNull();
        assertThat(res.getId()).isEqualTo(1L);
    }

    @Test
    void getProductBacklog_notFound() {
        when(repository.findById(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getProductBacklog(2L)).isInstanceOf(java.util.NoSuchElementException.class);
    }

    @Test
    void createProductBacklog_null_throws() {
        assertThatThrownBy(() -> service.createProductBacklog(null)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void getProductBacklog_nullId_throws() {
        assertThatThrownBy(() -> service.getProductBacklog(null)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void removeProductBacklog_callsDelete() {
        when(repository.findById(1L)).thenReturn(Optional.of(pb));
        when(userStoryRepository.existsById(any())).thenReturn(false);
        when(epicRepository.existsById(any())).thenReturn(false);
        doNothing().when(repository).deleteById(anyLong());

        // Ensure the method completes without throwing
        assertThatCode(() -> service.removeProductBacklog(1L)).doesNotThrowAnyException();
    }
}
