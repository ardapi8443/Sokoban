package sokoban.model;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.binding.LongBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.scene.control.skin.TextInputControlSkin;

import java.util.*;

public class Board4Play extends Board {
    private final Grid4Play grid4Play;
    private final IntegerBinding nbMoves;
    private final BooleanBinding boxOnGoal;

    private final LinkedList<Command> undo = new LinkedList<>();
    private final LinkedList<Command> redo = new LinkedList<>();
    private final LinkedList<Command> mushUndo = new LinkedList<>();
    private final LinkedList<Command> mushRedo = new LinkedList<>();
    private final SimpleBooleanProperty mustSetLabel = new SimpleBooleanProperty(false);

    public Board4Play(int width, int height, String content){
        grid4Play = new Grid4Play(width, height, content);

        boxOnGoal = grid4Play.getBoxOnGoalProperty().isEqualTo(grid4Play.goalInGridProperty()).and(grid4Play.getBoxOnGoalProperty().greaterThan(0));

        nbMoves = Bindings.createIntegerBinding(() ->
                        grid4Play.nbMovesProperty().get(),
                grid4Play.nbMovesProperty());

    }

    public ObservableList<Integer> moveBoxPosProperty() {
        return grid4Play.moveBoxPosProperty();
    }
    @Override
    public void updateCellSet (int line, int col, Artefact artefact) {
        if (!containPlayer(line, col) || !isPlayer(line, col, artefact)) {
            if (!isFull() || isGround(line, col, artefact)) {
                grid4Play.updateCellSet(line, col, artefact);
            }
        }
        haveChanged.set(true);
    }


    public void MovePlayerGrid(TextInputControlSkin.Direction direction) {
        grid4Play.tryMovePlayer(direction);
    }

    public void restoreState(Backup backup) {
        updateGridFromBackup(backup.getState(), this.getWithInt(), this.getHeightInt());
    }

    public LongBinding goalOnBoard() {
        return grid4Play.boxOnGoalProperty();
    }

    public IntegerBinding nbMovesProperty() {
        return nbMoves;
    }

    public LongBinding nbGoalProperty() {
        return grid4Play.goalInGridProperty();
    }

    public IntegerProperty getWith() {
        return grid4Play.widthProperty();
    }
    public IntegerProperty getHeight() {
        return grid4Play.heightProperty();
    }

    public int getWithInt() { return grid4Play.widthProperty().get();}
    public int getHeightInt() {return grid4Play.heightProperty().get();}

    public IntegerProperty getMovesProperty() { return  grid4Play.nbMovesProperty(); }
    public BooleanBinding getBoxOnGoalProperty () {
        return boxOnGoal;
    }
    public boolean isPlayer ( int line, int col, Artefact artefact){
        return grid4Play.isPlayer(line, col, artefact);
    }
    public boolean containPlayer ( int line, int col){
        return grid4Play.containPlayerProperty(line, col);
    }

    public ObservableSet<Artefact> getArtefacts(int line, int col) {
        return grid4Play.getArtefacts(line,col);
    }

    public Boolean isFull () {
        return isFull.get();
    }
    public void setMoves(int move) {
        grid4Play.setNbMoves(move);
    }

    public boolean isGround ( int line, int col, Artefact artefact){
        return grid4Play.isGround(line, col, artefact);
    }

    public String phrase() {
        return grid4Play.artefactsToChar();
    }

    public void updateGridFromBackup(String backup, int with, int height) {
        removePlayerAndBox();
        grid4Play.updateCellSetFromChars(backup,with,height);
        grid4Play.invalidate();
        mustSetLabel.set(true);
        mustSetLabel.set(false);
    }

    public SimpleBooleanProperty MustSetLabelProperty() {
        return mustSetLabel;
    }

    public void removePlayerAndBox() {
        grid4Play.removePlayerAndBox();
    }

    public boolean isBox(int line, int col) {
        return grid4Play.isBox(line,col);
    }

    public void initMushroomPos() {
        setMoves(getMovesProperty().get() + 10);
        grid4Play.showMushPos();
    }

    public void removeMushroomPos() {
        grid4Play.removeMushroomPos();
    }

    public boolean isMushroom(int line, int col) {
       return grid4Play.isMushroom(line,col);
    }

    public void hideMushroom() {
        grid4Play.removeMushroomPos();
    }

    // définir undo / commande
    public void undo() {

        if (!undo.isEmpty()) {
            Command command = undo.pop();
            command.undo();
            redo.push(command);
        }
        if (!mushUndo.isEmpty()) {
            Command command = mushUndo.pop();
            command.undo();
            mushRedo.push(command);
        }
    }

    public void redo() {
        if (!redo.isEmpty()) {  // Chaque replay coûte 1 mouvement.
            Command command = redo.pop();
            command.execute();
            undo.push(command);
        }
        if (!mushRedo.isEmpty()) {
            Command command = mushRedo.pop();
            command.execute();
            mushUndo.push(command);
        }
    }

    public void exchangePosBox() {

        setMoves(getMovesProperty().get() + 20);


        List<int[][]> oldPos = deepCopyListOfArrays(grid4Play.getPosBoxses());

        grid4Play.exchangePosBox();
        MustSetLabelProperty().set(true);
        MustSetLabelProperty().set(false);

        List<int[][]> newPos = deepCopyListOfArrays(grid4Play.getPosBoxses());

        Command exchangeCommand = new ExchangePosBoxCommand(this,oldPos,newPos);

        int[][] posMush = grid4Play.getPosMush();

        int oldRow = posMush[0][0];
        int oldCol = posMush[0][1];

        posMush = grid4Play.determOfMushPos();

        int newRow = posMush[0][0];
        int newCol = posMush[0][1];

        Command moveMushroom = new MoveMushroomCommand(this, oldRow, oldCol, newRow, newCol);

        moveMushroom.execute();
        mushUndo.push(moveMushroom);

        undo.push(exchangeCommand);
        redo.clear();
    }

    void redoBox(List<int [][]> exPosBox) {
        grid4Play.redoBox(exPosBox);
    }

    public void movePlayer(TextInputControlSkin.Direction direction) {
        int[] posPlayer = grid4Play.getPosPlayer();

        if (posPlayer != null && grid4Play.isValidMove(posPlayer[0], posPlayer[1], direction)) {
            Command moveCommand = new MovePlayerCommand(this, direction);
            moveCommand.execute();
            undo.push(moveCommand);
            redo.clear();
        }
    }

    public List<int[][]> deepCopyListOfArrays(List<int[][]> originalList) {
        List<int[][]> copy = new ArrayList<>();
        for (int[][] originalArray : originalList) {
            int[][] arrayCopy = new int[originalArray.length][];
            for (int i = 0; i < originalArray.length; i++) {
                arrayCopy[i] = Arrays.copyOf(originalArray[i], originalArray[i].length);
            }
            copy.add(arrayCopy);
        }
        return copy;
    }

    public void moveMushroomTo(int newRow, int newCol) {
        grid4Play.moveMushroomTo(newRow,newCol);
    }
}
