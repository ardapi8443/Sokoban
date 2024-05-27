package sokoban.viewmodel;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import sokoban.model.Board;
import sokoban.model.Board4Design;

public class GridViewModel4Design extends GridViewModel{

    private Board4Design board4Design;

    private SimpleIntegerProperty width = new SimpleIntegerProperty();
    private SimpleIntegerProperty height = new SimpleIntegerProperty();

    public IntegerProperty widthProperty() {
        return width;
    }

    public IntegerProperty heightProperty() {
        return height;
    }
    private final ArtefactViewModel artefactViewModel;

    GridViewModel4Design(Board4Design board, ArtefactViewModel artefactViewModel) {
        this.board4Design = board;
        this.artefactViewModel = artefactViewModel;
        configBindings();
    }

    public CellViewModel4Design getCellViewModel4Design(int line, int col) {
        return new CellViewModel4Design(line, col, board4Design, artefactViewModel);
    }
    public void configBindings() {
        width.bind(board4Design.getWith());
        height.bind(board4Design.getHeight());
    }

    public void isPossible(){
        board4Design.isPossible();
    }
    public void playPosComputer(){
        board4Design.playPosComputer();
    }
}
