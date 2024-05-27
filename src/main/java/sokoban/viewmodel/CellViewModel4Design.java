package sokoban.viewmodel;

import javafx.beans.binding.BooleanBinding;
import javafx.collections.ObservableSet;
import sokoban.model.Artefact;
import sokoban.model.Board4Design;

public class CellViewModel4Design extends CellViewModel {

    private final ArtefactViewModel artefactViewModel;
    private final Board4Design board;

    CellViewModel4Design(int line, int col, Board4Design board, ArtefactViewModel artefactViewModel) {
        super(line, col);
        this.board = board;
        this.artefactViewModel = artefactViewModel;
    }

    public void updateCellSet() {
        if (artefactViewModel.isPlayer() && board.getContainPlayer()) {
            board.removePlayerAndPlay(line,col, artefactViewModel.getCurrentArtefact());
        } else
            board.updateCellSet(line, col, artefactViewModel.getCurrentArtefact());
    }

    public void clear() {board.clear(line,col);}
    public void removeLastArtefact() {board.removeLastArtefact(line,col);}

    public BooleanBinding readyToPlayProperty() {return board.allConditionsMetProperty();}

    public ObservableSet<Artefact> getArtefacts() {
        return board.getArtefacts(line,col);
    }
}
