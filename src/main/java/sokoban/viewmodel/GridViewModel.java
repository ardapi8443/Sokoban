package sokoban.viewmodel;

import javafx.beans.property.*;


public abstract class GridViewModel {

    private final BooleanProperty haveChanged = new SimpleBooleanProperty(false);

    // Méthodes pour mettre à jour les propriétés

    public BooleanProperty haveChangedProperty() {
        return haveChanged;
    }

}
