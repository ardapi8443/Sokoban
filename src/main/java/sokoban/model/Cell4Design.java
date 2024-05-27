package sokoban.model;

class Cell4Design extends Cell{

    public Cell4Design() {
        super();
    }

    @Override
    public boolean isEmpty() {
        return artefactSet.size() == 1;
    }

    public void removeLastArtefact() {
        // contient ground et autre chose
        if (artefactSet.size() == 2) {
            resetSet();
        } else {
            // contient ground artefact et goal (possible de stack 3 si goal est dedans donc remove goal > 2)
            artefactSet.remove(new Goal());
        }
    }
}
