package sokoban.viewmodel;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.binding.LongBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import sokoban.model.Board4Design;
import sokoban.model.BoardData;

public class BoardViewModel4Design extends BoardViewModel {

    private Board4Design board4Design;
    private final ArtefactViewModel artefactViewModel;
    private GridViewModel4Design gridViewModel4Design;
    private String saveGrid;
    BoardData boardData;
    public BoardViewModel4Design (Board4Design board4Design) {
        this.board4Design = board4Design;
        artefactViewModel = new ArtefactViewModel();
        gridViewModel4Design = new GridViewModel4Design(board4Design, artefactViewModel);
        //to handle save and load
        boardData = new BoardData();

    }
    public void loadLevel(Integer level) {
        boardData =  boardData.loadLevel(level);
        this.board4Design = new Board4Design(boardData.getWidth(), boardData.getHeight(),boardData.getContent());
        gridViewModel4Design = new GridViewModel4Design(board4Design, artefactViewModel);
    }
    public void loadLevelFromString() {
        this.board4Design = new Board4Design(board4Design.getWith().get(), board4Design.getHeight().get(),saveGrid);
        gridViewModel4Design = new GridViewModel4Design(board4Design, artefactViewModel);
    }

    public void nnew(int width, int height) {
        this.board4Design = new Board4Design(width, height,"");
        gridViewModel4Design = new GridViewModel4Design(board4Design, artefactViewModel);
    }
    public void save() {
        board4Design.save();
    }
    public void dom() {
        board4Design.doom();
    }
    public void open() {
        board4Design.open();
    }
    public void saveGrid() {
        saveGrid = board4Design.phrase();
    }

    public String getSavedGrid() {
        return saveGrid;
    }

    public IntegerBinding maxFilledCells() {
        return board4Design.maxFilledCellsProperty();
    }
    public long maxPlayer() {
        return board4Design.maxPlayer();
    }
    public GridViewModel4Design getGridViewModel4Design() {
        return gridViewModel4Design;
    }
    public LongBinding filledCellsCountProperty() {
        return board4Design.filledCellsCountProperty();
    }
    public IntegerProperty getWithProperty() {
        return board4Design.getWith();
    }
    public IntegerProperty getHeightProperty() {
        return board4Design.getHeight();
    }
    public ArtefactViewModel getArtefactViewModel(){
        return artefactViewModel;
    }
    public LongBinding playerInProperty() {
        return board4Design.playerInGridProperty();
    }
    public BooleanBinding playerProperty() {
        return board4Design.containPlayerProperty();
    }
    public BooleanBinding getIsPossibleProperty(){
        return board4Design.getIsPossibleProperty();
    }
    public BooleanBinding matchGoalAndBox() {
        return board4Design.matchGoalAndBoxProperty();
    }
    public BooleanBinding boxInProperty() {
        return board4Design.containBoxProperty();
    }
    public BooleanBinding goalInProperty() {
        return board4Design.containGoalProperty();
    }
    public BooleanProperty haveChangedProperty() {
        return board4Design.haveChangedProperty();
    }
    public BooleanBinding readyToPlayProperty() {return board4Design.allConditionsMetProperty();}
}
