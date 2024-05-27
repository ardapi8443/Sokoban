package sokoban.model;

class Wall extends Artefact {

    Wall() {
        super(ArtefactMode.WALL);
    }

    public boolean equals(Object o) {
        return o instanceof Wall;
    }

    @Override
    public int hashCode() {
        return 0;
    }
}