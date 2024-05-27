package sokoban.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import java.util.*;

abstract class Cell {
    public abstract boolean isEmpty ();

    protected final ObservableSet<Artefact> artefactSet = FXCollections.observableSet();

    public ObservableSet<Artefact> getArtefactSet() {
        return artefactSet;
    }

    public Cell() {
        artefactSet.add(new Ground());
    }

    public void removePlayer(Artefact player) {
        artefactSet.remove(player);
    }

    public ObservableSet<Artefact> artefactSetProperty(){
        return this.artefactSet;
    }

    boolean containPlayer() {
        return artefactSet.contains(new Player());
    }

    boolean boxOnGoal() {
        return artefactSet.contains(new Box()) && artefactSet.contains(new Goal());
    }

    boolean containBox() {
        return artefactSet.contains(new Box());
    }

    boolean containWall() {
        return artefactSet.contains(new Wall());
    }
    boolean containGoal() {
        return artefactSet.contains(new Goal());
    }

    public boolean contains(Artefact a){
        return artefactSet.contains(a);
    }

    public boolean removeGoal() {
        return artefactSet.remove(new Goal());
    }

    public void addGoal() {
        artefactSet.add(new Goal());
    }

    public void addArtefact(Artefact artefact) {
        if (isWall(artefact)) {
            resetAndAdd(artefact);
        } else {
            boolean containsGoal = removeGoal();
            artefactSet.add(artefact);
            // rajoute le Goal par dessus le dernière artefact dans le Set
            if (containsGoal) {
                artefactSet.add(new Goal());
            }
        }

        if (isGround(artefact)) {
            resetSet();
        }

        else if (isBox(artefact)) {
            // Supprime le Player du Set
            artefactSet.remove(new Player());
        }

        else if (isPlayer(artefact) && containBox()) {
            resetAndAdd(artefact);
        }

        else if (isPlayer(artefact) && containWall()) {
            resetAndAdd(artefact);
        }
        else if (isGoal(artefact) && containWall()) {
            resetAndAdd(artefact);
        }
    }
    public char readCell() {
        if (containWall()) {
            return '#';
        } else if (containGoal()) {
            if (containPlayer()) {
                return '+';
            } else if (containBox()) {
                return '*';
            } else
                return '.';
        } else if (containBox()) {
            return '$';
        } else if (containPlayer()) {
            return '@';
        } else if (containMushroom()) {
            return 'ù';
        }
        return ' ';
    }

        public void removeWall () {
            artefactSet.remove(new Wall());
        }

        public boolean isGround (Artefact artefact){
            return artefact.equals(new Ground());
        }

        public boolean isBox (Artefact artefact){
            return artefact.equals(new Box());
        }

        public boolean isBox() {
            return artefactSet.contains(new Box()) && artefactSet.size() == 2;
        }

        public boolean isGoal (Artefact artefact){
            return artefact.equals(new Goal());
        }

        public boolean isPlayer (Artefact artefact){
            return artefact.equals(new Player());
        }

        public boolean isPlayer() {return artefactSet.contains(new Player());}
        public boolean containMushroom() {return  artefactSet.contains(new Mushroom()); }
        public boolean isGround() {return artefactSet.contains(new Ground()) && artefactSet.size() == 1 ;}
        private boolean isWall (Artefact artefact){
            return artefact.equals(new Wall());
        }

        @Override
        public String toString () {
            String res = "";
            for (Artefact a : artefactSet) {
                res += a.toString() + ", ";
            }
            return res;
        }

        @Override
        public boolean equals (Object o){
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Cell cell = (Cell) o;
            return Objects.equals(artefactSet, cell.artefactSet);
        }

        @Override
        public int hashCode () {
            return Objects.hash(artefactSet);
        }

        public void removePlayer () {
            artefactSet.remove(new Player());
        }

        public void addPlayer () {
            artefactSet.add(new Player());
        }

        public void addBox () {
            artefactSet.add(new Box());
        }

        public void removeBox () {
            artefactSet.remove(new Box());
        }

        public void resetSet () {
            artefactSet.clear();
            artefactSet.add(new Ground());
        }

        void resetAndAdd (Artefact artefact){
            resetSet();
            artefactSet.add(artefact);
        }


    public void addArtefactFromChar(char c) {
        switch (c) {
            case '#': // Mur
                addArtefact(new Wall());
                break;
            case '+': // Joueur sur la cible
                addArtefact(new Goal());
                addArtefact(new Player());
                break;
            case '*': // Boîte sur la cible
                addArtefact(new Goal());
                addArtefact(new Box());
                break;
            case '.': // Cible vide
                addArtefact(new Goal());
                break;
            case '$': // Boîte
                addArtefact(new Box());
                break;
            case '@': // Joueur
                addArtefact(new Player());
                break;
            case 'ù': // Champignon
                addArtefact(new Mushroom());
            case ' ':
                break;
        }
    }

}