package io.younsai.gestionprojetsagile.common.contracts;

import io.younsai.gestionprojetsagile.model.UserStory;

public interface UserStoryCrud {
    UserStory ajouter();
    UserStory modifier();
    void supprimer();


}
