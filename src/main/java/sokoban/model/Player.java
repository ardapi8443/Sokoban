package sokoban.model;

class Player extends Artefact {

    Player() {
        super(ArtefactMode.PLAYER);
    }

    public boolean equals(Object o) {
        return o instanceof Player;
    }

    @Override
    public int hashCode() {
        return 0;
    }
}