package sokoban.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.binding.DoubleBinding;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

abstract class CellView extends StackPane {
    final DoubleBinding widthProperty;
    final ObservableList<Image> images = FXCollections.observableArrayList();

    CellView(DoubleBinding cellWidthProperty) {
        this.widthProperty = cellWidthProperty;
        setAlignment(Pos.CENTER);
    }

    abstract void layoutControls();
    abstract void configureBindings();
    abstract void updateImages();

    abstract void mouseActions();

    public ObservableList<Image> getImages() {
        return images;
    }


}
