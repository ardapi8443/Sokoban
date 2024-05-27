package sokoban.model;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.LongBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableSet;
import javafx.scene.control.skin.TextInputControlSkin;
import javafx.collections.*;
import java.util.*;
import java.util.function.Predicate;

abstract class Grid {
    final IntegerProperty width = new SimpleIntegerProperty(15);
    final IntegerProperty height = new SimpleIntegerProperty(10);
    final long nbPlayer = 1;
    ListChangeListener<Integer> listener;
    final static int MAX_WIDTH_SIZE = 50; // x 50º
    final static int MAX_HEIGHT_SIZE = 50;
    LongBinding goalInGrid;
    public Grid(int width, int height) {
        setDimensions(width, height);
    }
    public int getWidth() {
        return width.get();
    }
    public int getHeight() {
        return height.get();
    }
    public void setHeightProperty(int height) {
        this.height.set(height);
    }
    public IntegerProperty widthProperty() {
        return width;
    }
    public void setWidthProperty(int width) {
        this.width.set(width);
    }
    public IntegerProperty heightProperty() {
        return height;
    }
    public void setDimensions(int newWidth, int newHeight) {
        setWidthProperty(newWidth);
        setHeightProperty(newHeight);
    }

    abstract boolean containPlayerProperty(int line, int col);

    abstract boolean isGround(int line, int col, Artefact artefact);

//    public boolean isEmpty(int line, int col) {
//        return matrix[line][col].isEmpty();
//    }
}
