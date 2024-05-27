package sokoban.model;

class Mushroom extends Artefact {

    Mushroom() {
        super(ArtefactMode.MUSHROOM);
    }

    public boolean equals(Object o) {
        return o instanceof Mushroom;
    }

    @Override
    public int hashCode() {
        return 0;
    }
}