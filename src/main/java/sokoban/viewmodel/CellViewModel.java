package sokoban.viewmodel;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ObservableSet;
import sokoban.model.Artefact;

public abstract class CellViewModel {
    private static final double DEFAULT_SCALE = 1;

    final int line, col;

    private final SimpleDoubleProperty scale = new SimpleDoubleProperty(DEFAULT_SCALE);

    public abstract ObservableSet<Artefact> getArtefacts();

    CellViewModel(int line, int col) {
        this.line = line;
        this.col = col;
    }

    public SimpleDoubleProperty scaleProperty() {
        return scale;
    }

}
