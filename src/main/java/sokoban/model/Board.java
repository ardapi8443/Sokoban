package sokoban.model;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.binding.LongBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableSet;
import javafx.scene.control.skin.TextInputControlSkin;

public abstract class Board {

    FileHandler fileHandler;
    BoardData boardData;
    final BooleanProperty haveChanged = new SimpleBooleanProperty(false);;
    public abstract IntegerProperty getWith();

    public abstract IntegerProperty getHeight();
    BooleanBinding isFull;
    abstract boolean containPlayer ( int line, int col);
    abstract void updateCellSet(int line, int col, Artefact artefact);

    abstract boolean isPlayer ( int line, int col, Artefact artefact);
    abstract boolean isGround ( int line, int col, Artefact artefact);

}