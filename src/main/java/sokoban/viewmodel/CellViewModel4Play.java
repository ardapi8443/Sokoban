package sokoban.viewmodel;

import javafx.collections.ObservableSet;
import sokoban.model.Artefact;
import sokoban.model.Board4Play;

public class CellViewModel4Play extends CellViewModel{

    private final Board4Play board;

    CellViewModel4Play(int line, int col, Board4Play board) {
        super(line, col);
        this.board = board;
    }

    public ObservableSet<Artefact> getArtefacts() {
        return board.getArtefacts(line,col);
    }

    public boolean isBox() { return board.isBox(line,col); }

    public boolean checkIfMushroom() {
       return board.isMushroom(line,col);
    }

    public void exchangePosBox() {
        board.exchangePosBox();

    }

}
