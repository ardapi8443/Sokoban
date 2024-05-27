package sokoban.model;

class Goal extends Artefact {

    Goal() {
        super(ArtefactMode.GOAL);
    }

    public boolean equals(Object o) {
        return o instanceof Goal;
    }
    @Override
    public int hashCode() {
        return 0;
    }
}