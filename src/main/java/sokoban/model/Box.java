package sokoban.model;

class Box extends Artefact {

    Box() {
        super(ArtefactMode.BOX);

    }

    public static ArtefactMode  artefactMode = ArtefactMode.BOX;

    @Override
    public boolean equals(Object o) {
        return o instanceof Box;
    }
    @Override
    public int hashCode() {
        return 0;
    }

}