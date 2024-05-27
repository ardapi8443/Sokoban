package sokoban.viewmodel;

import sokoban.model.Artefact;
import sokoban.model.ArtefactMode;
import static sokoban.model.ArtefactMode.GROUND;

public class ArtefactViewModel {
    private Artefact currentArtefact ;

    public ArtefactViewModel(){
        this.currentArtefact = Artefact.create(GROUND);
    }

    public Artefact getCurrentArtefact() {
        return currentArtefact;
    }

    public void setCurrentArtefact(ArtefactMode artefactMode) {
        this.currentArtefact = currentArtefact.create(artefactMode);
        //passe pas board
    }

    public Boolean isPlayer() {
        return currentArtefact.getArtefactMode() == ArtefactMode.PLAYER;
    }

}
