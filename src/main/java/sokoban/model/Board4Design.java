package sokoban.model;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.binding.LongBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableSet;
import sokoban.viewmodel.GridViewModel;

public class Board4Design extends Board {
    private final Grid4Design grid4Design;
    private final BooleanBinding containPlayer;
    private final BooleanBinding containBox;
    private final BooleanBinding containGoal;
    private final BooleanBinding isPossible;
    private final BooleanBinding matchGoalAndBox;
    private final BooleanProperty haveChanged = new SimpleBooleanProperty(false);;

    IntegerBinding maxFilledCells;


    public Board4Design(int width,int height, String content){

        grid4Design = new Grid4Design(width,height,content);

        containPlayer = grid4Design.playerInGridProperty().isEqualTo(grid4Design.nbPlayer);
        containBox = grid4Design.boxInGridProperty().greaterThan(0);
        containGoal = grid4Design.goalInGridProperty().greaterThan(0);
        isPossible = grid4Design.getIsPossibleProperty().not();
        matchGoalAndBox = grid4Design.boxInGridProperty().isEqualTo(grid4Design.goalInGridProperty());

        this.fileHandler = new FileHandler(this);

        maxFilledCells = Bindings.createIntegerBinding(() ->
                        (int) ((double) (grid4Design.widthProperty().get() * grid4Design.heightProperty().get()) / 2),
                grid4Design.widthProperty(),
                grid4Design.heightProperty());

        isFull = grid4Design.filledCellsCountProperty().isEqualTo(maxFilledCellsProperty());
    }

    @Override
    public void updateCellSet (int line, int col, Artefact artefact) {
        if (!containPlayer() || !isPlayer(line, col, artefact)) {
            if (!isFull() || isGround(line, col, artefact)) {
                grid4Design.updateCellSet(line, col, artefact);
            }
        }
        haveChanged.set(true);
    }

    public void clear(int line, int col) {
        grid4Design.clear(line,col);
    }
    public void removeLastArtefact(int line, int col) {
        grid4Design.removeLastArtefact(line,col);
    }
    public BooleanBinding allConditionsMetProperty() {
        return containPlayerProperty()
                .and(matchGoalAndBoxProperty())
                .and(containBoxProperty())
                .and(containGoalProperty());
    }

    public void open() {
        boardData = boardData.readBoardFromFile();
        String content = boardData.getContent();
        int height = boardData.getHeight();
        int width = boardData.getWidth();
        grid4Design.updateCellSetFromChars(content,width,height);
        grid4Design.invalidate();
    }

    public void doom() {
        grid4Design.removeAllArtefact();
    }
    public void save () {
        fileHandler.save();
    }
    public String phrase() {
        return grid4Design.artefactsToChar();
    }

    public void removePlayerAndPlay(int line,int col,Artefact artefact) {
        grid4Design.removePlayer(artefact);
        grid4Design.updateCellSet(line,col,artefact);
    }
    public void updateCellSetFromChars(String content) {
        grid4Design.updateCellSetFromChars(content);
    }
    public IntegerBinding maxFilledCellsProperty() {
        return maxFilledCells;
    }

    public long maxPlayer () {
        return grid4Design.nbPlayer;
    }

    public Boolean isFull () {
        return isFull.get();
    }
    public ObservableSet<Artefact> getArtefacts(int line, int col) {
        return grid4Design.getArtefacts(line,col);
    }

    public Boolean containPlayer () {
        return containPlayer.get();
    }
    public ObservableSet<Artefact> artefactSetProperty (int line, int col){
        return grid4Design.artefactSetProperty(line, col);
    }
    public Boolean getContainPlayer () {
        return containPlayer.get();
    }

    public BooleanBinding containPlayerProperty () {
        return containPlayer;
    }

    public BooleanBinding containBoxProperty () {
        return containBox;
    }

    public BooleanBinding matchGoalAndBoxProperty() {
        return matchGoalAndBox;
    }

    public BooleanBinding containGoalProperty () {
        return containGoal;
    }

    public BooleanBinding getIsPossibleProperty() {
        return isPossible;
    }

    public LongBinding filledCellsCountProperty () {
        return grid4Design.filledCellsCountProperty();
    }

    public LongBinding playerInGridProperty () {
        return grid4Design.playerInGridProperty();
    }

    public boolean isEmpty ( int line, int col){
        return grid4Design.isEmpty(line, col);
    }

    public boolean isPlayer ( int line, int col, Artefact artefact){
        return grid4Design.isPlayer(line, col, artefact);
    }

    public boolean containPlayer ( int line, int col){
        return grid4Design.containPlayerProperty(line, col);
    }


    public boolean isGround ( int line, int col, Artefact artefact){
        return grid4Design.isGround(line, col, artefact);
    }

    public IntegerProperty getWith() {
        return grid4Design.widthProperty();
    }
    public IntegerProperty getHeight() {
        return grid4Design.heightProperty();
    }
    public BooleanProperty haveChangedProperty() {
        return haveChanged;
    }

    public void isPossible(){
        grid4Design.isPossible();
    }

    public void playPosComputer(){
        grid4Design.playPosComputer();
    }
}
