package io.younsai.gestionprojetsagile.service.impl;

import io.younsai.gestionprojetsagile.model.Epic;
import io.younsai.gestionprojetsagile.model.ProductBacklog;
import io.younsai.gestionprojetsagile.model.UserStory;
import io.younsai.gestionprojetsagile.repository.EpicRepository;
import io.younsai.gestionprojetsagile.repository.ProductBacklogRepository;
import io.younsai.gestionprojetsagile.repository.UserStoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EpicServiceImplTest {

    @Mock
    private EpicRepository epicRepository;
    @Mock
    private UserStoryRepository userStoryRepository;
    @Mock
    private ProductBacklogRepository productBacklogRepository;

    @InjectMocks
    private EpicServiceImpl service;

    private Epic epic;
    private UserStory us;
    private ProductBacklog pb;

    @BeforeEach
    void setUp() {
        epic = Epic.builder().titre("E1").build();
        epic.setId(1L);
        epic.setListeDesUserStories(new ArrayList<>());

        pb = ProductBacklog.builder().nom("PB").build();
        pb.setId(10L);
        pb.setListeDesEpics(new ArrayList<>());

        us = UserStory.builder().titre("US1").build();
        us.setId(100L);
        us.setProductBacklog(pb);
    }

    @Test
    void createEpic_happy() {
        when(epicRepository.save(any())).thenReturn(epic);
        Epic out = service.createEpic(new Epic());
        assertThat(out).isNotNull();
        verify(epicRepository).save(any());
    }

    @Test
    void getEpic_found() {
        when(epicRepository.findById(1L)).thenReturn(Optional.of(epic));
        Epic out = service.getEpic(1L);
        assertThat(out.getId()).isEqualTo(1L);
    }

    @Test
    void getEpic_notFound() {
        when(epicRepository.findById(2L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.getEpic(2L)).isInstanceOf(java.util.NoSuchElementException.class);
    }

    @Test
    void linkUserStoryToEpic_happy() {
        // epic is attached to product backlog via productBacklogRepository.findByListeDesEpics_Id
        when(epicRepository.findById(1L)).thenReturn(Optional.of(epic));
        when(userStoryRepository.findById(100L)).thenReturn(Optional.of(us));
        when(productBacklogRepository.findByListeDesEpics_Id(1L)).thenReturn(Optional.of(pb));
        when(userStoryRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        UserStory saved = service.linkUserStoryToEpic(1L, 100L);
        assertThat(saved).isNotNull();
        verify(epicRepository, atLeastOnce()).save(any());
    }
}

