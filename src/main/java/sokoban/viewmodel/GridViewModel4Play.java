package sokoban.viewmodel;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.skin.TextInputControlSkin;
import sokoban.model.Board;
import sokoban.model.Board4Play;

public class GridViewModel4Play extends GridViewModel {

    private Board4Play board4Play;

    private SimpleIntegerProperty width = new SimpleIntegerProperty();
    private SimpleIntegerProperty height = new SimpleIntegerProperty();
    private SimpleBooleanProperty mustSetLabel;

    public IntegerProperty widthProperty() {
        return width;
    }

    public IntegerProperty heightProperty() {
        return height;
    }

    GridViewModel4Play(Board4Play board) {
        this.board4Play = board;

        configBindings();
    }

    public ObservableList<Integer> moveBoxPosProperty(){
        return board4Play.moveBoxPosProperty();
    }
    public void undo() { board4Play.undo();}

    public void redo() {
        board4Play.redo();
    }


    public CellViewModel4Play getCellViewModel4Play(int line, int col) {
        return new CellViewModel4Play(line, col, board4Play);
    }

    public void movePlayer(TextInputControlSkin.Direction direction) {
        board4Play.movePlayer(direction);
    }

    public void configBindings() {
        mustSetLabel = board4Play.MustSetLabelProperty();
        width.bind(board4Play.getWith());
        height.bind(board4Play.getHeight());
    }

    public SimpleBooleanProperty MustSetLabelProperty() {
        return mustSetLabel;
    }

}
