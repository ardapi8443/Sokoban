package sokoban.model;

public abstract class Artefact {

//    @Override
//    public abstract String toString();

    private final ArtefactMode artefactMode;
    Artefact(ArtefactMode artefactMode) {
        this.artefactMode = artefactMode;
    }

    public static Artefact create(ArtefactMode artefactMode){
        switch (artefactMode) {
            case GROUND -> {
                return new Ground();
            }
            case BOX -> {
                return new Box();
            }
            case PLAYER -> {
                return new Player();
            }
            case WALL -> {
                return new Wall();
            }
            case GOAL -> {
                return new Goal();
            }
            case MUSHROOM -> {
                return new Mushroom();
            }
        }
        return null;
    }

    public ArtefactMode getArtefactMode() {
        return artefactMode;
    }
}
