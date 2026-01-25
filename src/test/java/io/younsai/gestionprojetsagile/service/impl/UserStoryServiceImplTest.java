package io.younsai.gestionprojetsagile.service.impl;

import io.younsai.gestionprojetsagile.model.ProductBacklog;
import io.younsai.gestionprojetsagile.model.UserStory;
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
class UserStoryServiceImplTest {

    @Mock
    private ProductBacklogRepository productBacklogRepository;
    @Mock
    private UserStoryRepository userStoryRepository;

    @InjectMocks
    private UserStoryServiceImpl service;

    private ProductBacklog pb;
    private UserStory us;

    @BeforeEach
    void setUp() {
        pb = ProductBacklog.builder().nom("PB").build();
        pb.setId(10L);
        pb.setUserStories(new ArrayList<>());

        us = UserStory.builder().titre("US1").description("desc").build();
        us.setId(100L);
    }

    @Test
    void createUserStory_happyPath() {
        when(productBacklogRepository.findById(10L)).thenReturn(Optional.of(pb));
        when(userStoryRepository.save(any())).thenAnswer(inv -> {
            UserStory arg = inv.getArgument(0);
            arg.setId(100L);
            return arg;
        });

        UserStory saved = service.createUserStory(10L, new UserStory());
        assertThat(saved).isNotNull();
        verify(productBacklogRepository).save(any());
    }

    @Test
    void createUserStory_backlogNotFound() {
        when(productBacklogRepository.findById(20L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.createUserStory(20L, new UserStory())).isInstanceOf(java.util.NoSuchElementException.class);
    }

    @Test
    void updateUserStory_happyPath() {
        UserStory existing = UserStory.builder().titre("old").description("old").build();
        existing.setId(100L);
        when(userStoryRepository.findById(100L)).thenReturn(Optional.of(existing));
        when(userStoryRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        UserStory patch = UserStory.builder().titre("new").build();
        UserStory out = service.updateUserStory(100L, patch);
        assertThat(out.getTitre()).isEqualTo("new");
    }

    @Test
    void updateUserStory_notFound() {
        when(userStoryRepository.findById(200L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.updateUserStory(200L, new UserStory())).isInstanceOf(java.util.NoSuchElementException.class);
    }

    @Test
    void removeUserStory_happyPath() {
        when(userStoryRepository.findById(100L)).thenReturn(Optional.of(us));
        doNothing().when(userStoryRepository).deleteById(100L);

        service.removeUserStory(100L);
        verify(userStoryRepository).deleteById(100L);
    }
}
