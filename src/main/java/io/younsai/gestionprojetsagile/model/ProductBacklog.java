package io.younsai.gestionprojetsagile.model;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductBacklog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "productBacklog")
    private List<Epic> listeDesEpics = new ArrayList<>();

    @OneToMany(mappedBy = "productBacklog")
    private List<UserStory> userStories = new ArrayList<>();

/*    public UserStory ajouterUserStory(UserStory userStory){
        userStories.add(userStory);
        userStory.setProductBacklog(this);
        return userStory;
    }

    public List<UserStory> filtrerPar(Comparator<UserStory> comparator){
        userStories.sort(comparator);
        return userStories;
    }

    public List<UserStory> trierPar() {
        return null;
    }

    public UserStory modifierUserStory(UserStory userStory){
        userStories.set(userStories.indexOf(userStory), userStory);
        userStory.setProductBacklog(this);
        return userStory;
    }

    public Epic ajouterEpic(Epic epic){
        listeDesEpics.add(epic);
        return epic;
    }

    public Epic modifierEpic(Epic epic){
        listeDesEpics.set(listeDesEpics.indexOf(epic), epic);
        return epic;
    }*/

}


