package sokoban.viewmodel;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.binding.LongBinding;
import javafx.beans.property.IntegerProperty;
import sokoban.model.Board4Play;

public class BoardViewModel4Play extends BoardViewModel{
    private String saveGrid;
    private Board4Play board4Play;
    private final ArtefactViewModel artefactViewModel;
    private GridViewModel4Play gridViewModel4Play;

    public BoardViewModel4Play (Board4Play board4Play) {
        this.board4Play = board4Play;
        artefactViewModel = new ArtefactViewModel();
        gridViewModel4Play = new GridViewModel4Play(board4Play);

    }

    public GridViewModel4Play getGridViewModel4Play() {
        return gridViewModel4Play;
    }
    public IntegerProperty getWithProperty() {
        return board4Play.getWith();
    }
    public IntegerProperty getHeightProperty() {
        return board4Play.getHeight();
    }
    public IntegerBinding getMovesProperty() {return board4Play.nbMovesProperty();}
    public ArtefactViewModel getArtefactViewModel(){
        return artefactViewModel;
    }
    public BooleanBinding boxOnGoalProperty() {
        return board4Play.getBoxOnGoalProperty();
    }
    public LongBinding boxOnGoal() {
        return board4Play.goalOnBoard();
    }
    public LongBinding nbGoalProperty() {
        return board4Play.nbGoalProperty();
    }
    public void loadLevelFromString() {
        this.board4Play = new Board4Play(board4Play.getWith().get(), board4Play.getHeight().get(),saveGrid);
        gridViewModel4Play = new GridViewModel4Play(board4Play);
    }
    public void saveGrid() {
        saveGrid = board4Play.phrase();
    }

    public String getSavedGrid() {
        return saveGrid;
    }

    public void initMushroomPos() {
        board4Play.initMushroomPos();
    }

    public void hideMushroom() {
        board4Play.hideMushroom();
    }
}
