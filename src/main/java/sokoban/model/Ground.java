package sokoban.model;

class Ground extends Artefact {

    Ground() {
        super(ArtefactMode.GROUND);
    }

    public boolean equals(Object o) {
        return o instanceof Ground;
    }
    @Override
    public int hashCode() {
        return 0;
    }
}