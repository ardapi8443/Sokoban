package sokoban.model;

class Cell4Play extends Cell {

    public Cell4Play() {
        super();
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    public void addMushroom() {
        artefactSet.add(new Mushroom());
    }

    public void removeMushroom() {
        artefactSet.remove(new Mushroom());
    }
}
