package sokoban.model;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.LongBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.*;
import javafx.scene.control.skin.TextInputControlSkin;

import java.util.*;
import java.util.function.Predicate;

class Grid4Play extends Grid {
    private final Cell4Play[][] matrix4Play;

    private final LongBinding boxOnGoal;
    private int[][] posMush;

    private List<int [][]> posBoxses = new ArrayList<>();

    private final IntegerProperty nbMoves = new SimpleIntegerProperty(0);
    private final ObservableList<Integer> moveBoxPos = FXCollections.observableArrayList();
    private ListChangeListener<Integer> listener4Array;

    public Grid4Play(int width, int height, String content) {
        super(width,height);
        matrix4Play = new Cell4Play[MAX_WIDTH_SIZE][MAX_HEIGHT_SIZE];
        for (int i = 0; i < MAX_WIDTH_SIZE; ++i) {
            matrix4Play[i] = new Cell4Play[MAX_HEIGHT_SIZE];
            for (int j = 0; j < MAX_HEIGHT_SIZE; ++j) {
                matrix4Play[i][j] = new Cell4Play();
            }
        }

        updateCellSetFromChars(content, width, height);
        boxOnGoal = countElemInGrid(Cell::boxOnGoal);
        goalInGrid = countElemInGrid(Cell::containGoal);
        posMush = determOfMushPos();
        posBoxses = putBoxIntoMap();
        listener4Array = change -> {

        };
    }

    public ObservableList<Integer> moveBoxPosProperty() {
        return moveBoxPos;
    }

    public LongBinding boxOnGoalProperty() {
        return boxOnGoal;
    }

    ObservableSet<Artefact> artefactSetProperty(int line, int col) {
        return matrix4Play[line][col].artefactSetProperty();
    }

    ObservableSet<Artefact> getArtefacts(int line, int col) {
        return matrix4Play[line][col].getArtefactSet();
    }
    void invalidate() {
        boxOnGoal.invalidate();
    }
    int[][] getPosMush() {
        return posMush;
    }
    boolean isPlayer(int line, int col, Artefact artefact) {
        return matrix4Play[line][col].isPlayer(artefact);
    }
    boolean isPlayer(int line, int col) {
        return matrix4Play[line][col].isPlayer();
    }
    boolean isGround(int line, int col, Artefact artefact) {
        return matrix4Play[line][col].isGround(artefact);
    }

    boolean isGround(int line, int col) {
        return matrix4Play[line][col].isGround();
    }

    void updateCellSet(int line, int col, Artefact updateCellSeterValue) {
        matrix4Play[line][col].addArtefact(updateCellSeterValue);
        invalidate();
    }

    boolean containPlayerProperty(int line, int col) {
        return matrix4Play[line][col].containPlayer();
    }
    LongBinding countElemInGrid(Predicate<Cell4Play> filter) {
        return Bindings.createLongBinding(() ->
                Arrays.stream(matrix4Play)
                        .flatMap(Arrays::stream)
                        .filter(filter)
                        .count());
    }

    void clear(int line, int col) {
        matrix4Play[line][col].resetSet();
        invalidate();
    }

    void removeMush(int line, int col) {
        matrix4Play[line][col].removeMushroom();
    }


    void tryMovePlayer(TextInputControlSkin.Direction direction) {

        int [] x = getPosPlayer();
        int newRow = x[0], newCol = x[1];
        int oldRow = x[0], oldCol = x[1];

            switch (direction) {
                case UP -> newRow--;
                case DOWN -> newRow++;
                case LEFT -> newCol--;
                case RIGHT -> newCol++;
            }

            boolean containsGoals = matrix4Play[newRow][newCol].removeGoal();

            if (matrix4Play[newRow][newCol].containBox()) {

                int boxNewRow = newRow + (newRow - oldRow);
                int boxNewCol = newCol + (newCol - oldCol);

                boolean containsGoal = matrix4Play[boxNewRow][boxNewCol].removeGoal();
                moveArtefact(new Box(), boxNewRow, boxNewCol, newRow, newCol);
                moveArtefact(new Player(), newRow, newCol, oldRow, oldCol);

                if (containsGoal)
                    matrix4Play[boxNewRow][boxNewCol].addGoal();

            } else
                moveArtefact(new Player(), newRow, newCol, oldRow, oldCol);

            if (containsGoals)
                matrix4Play[newRow][newCol].addGoal();
            invalidate();
    }

    boolean isValidMove(int currentRow, int currentCol, TextInputControlSkin.Direction direction) {
        int newRow = currentRow, newCol = currentCol;
        switch (direction) {
            case UP -> newRow--;
            case DOWN -> newRow++;
            case LEFT -> newCol--;
            case RIGHT -> newCol++;
        }

        if (isValidPosition(newRow, newCol)) {
            if (matrix4Play[newRow][newCol].containBox()) {

                int nextBoxRow = newRow + (newRow - currentRow);
                int nextBoxCol = newCol + (newCol - currentCol);

                return isValidPosition(nextBoxRow, nextBoxCol) && !matrix4Play[nextBoxRow][nextBoxCol].containBox();
            }
            return true;
        }
        return false;
    }

    boolean isValidPosition(int row, int col) {
        if (row < 0 || row >= getHeight() || col < 0 || col >= getWidth()) {
            return false;
        }
        return !matrix4Play[row][col].containWall();
    }

    IntegerProperty nbMovesProperty() {
        return nbMoves;
    }

    void setNbMoves(int nbMoves) {
        this.nbMoves.set(nbMoves);
    }

    void moveArtefact(Artefact artefact, int newLine, int newCol, int oldLine, int oldCol) {
        if (artefact.equals(new Box())) {
            matrix4Play[newLine][newCol].addBox();
            matrix4Play[oldLine][oldCol].removeBox();

            moveBoxPos.removeListener(listener4Array);
            moveBoxPos.clear();
            moveBoxPos.addListener(listener4Array);
            moveBoxPos.addAll(newLine, newCol, oldLine, oldCol);

        }
        if (artefact.equals(new Player())) {
            matrix4Play[newLine][newCol].addPlayer();
            matrix4Play[oldLine][oldCol].removePlayer();
        }
    }

    LongBinding getBoxOnGoalProperty() {
        return boxOnGoal;
    }
    LongBinding goalInGridProperty() {
        return goalInGrid;
    }
    List<int [][]> getPosBoxses() {
        return posBoxses;
    }
    String artefactsToChar() {
        String res = "";
        for (int i = 0; i < getHeight(); ++i) {
            for (int j = 0; j < getWidth(); ++j) {
                res += matrix4Play[i][j].readCell();
            }
            res += "\n";
        }
        return res;
    }

    void updateCellSetFromChars(String res, int width, int height) {
        // supprime le contenu des artefacts avant de ré-injecter le contenu à l'ouverture d'un autre niveau
        // Diviser la chaîne en lignes
        String[] rows = res.split("\n");
        // Parcourir chaque ligne et chaque colonne de la grille

        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                char artefactChar = rows[i].charAt(j);
                matrix4Play[i][j].addArtefactFromChar(artefactChar);
            }
        }
    }

    boolean isMushroom(int line, int col) {
        int x = posMush[0][0];
        int y = posMush[0][1];

        return x == line && y == col;
    }

    boolean isBox(int line, int col) {
        return matrix4Play[line][col].isBox();
    }

    boolean isBoxOnGoal(int line, int col) {
        return matrix4Play[line][col].boxOnGoal();
    }

    void addMushroom(int line, int col) {
        matrix4Play[line][col].addMushroom();
    }

    void removePlayerAndBox() {
        for (int i = 0; i < getHeight(); ++i) {
            for (int j = 0; j < getWidth(); ++j) {
                if(isBox(i,j) || (isBoxOnGoal(i,j))) {
                    matrix4Play[i][j].removeBox();
                }
                else if (isPlayer(i,j)) {
                    matrix4Play[i][j].removePlayer();
                }
            }
        }
    }

    void showMushPos() {
        int x = posMush[0][0];
        int y = posMush[0][1];
        addMushroom(x, y);
    }

    List<int [][]> putBoxIntoMap() {
        for (int i = 0; i < getHeight(); ++i) {
            for (int j = 0; j < getWidth(); ++j) {
                if(isBox(i,j) || isBoxOnGoal(i,j)) {
                    int[][] boxCoordinates = {{i, j}};
                    posBoxses.add(boxCoordinates);
                }
            }
        }
        return posBoxses;
    }


     int[][] determOfMushPos() {
        Random rand = new Random();
        int x,y;

        do {
            x = rand.nextInt(getHeight());
            y = rand.nextInt(getWidth());
        } while (!isGround(x,y));

        int[][] pos = new int[1][2];

        pos[0][0] = x;
        pos[0][1] = y;

        return pos;
    }

    int[][] determOfBoxPos() {
        Random rand = new Random();
        int x,y;

        do {
            x = 1 + rand.nextInt(getHeight() - 2);
            y = 1 + rand.nextInt(getWidth() - 2);
        } while (!isGround(x,y));

        int[][] pos = new int[1][2];

        pos[0][0] = x;
        pos[0][1] = y;

        return pos;
    }

    void removeMushroomPos() {
        int x = posMush[0][0];
        int y = posMush[0][1];
        removeMush(x,y);
    }

    void exchangePosBox() {

        clearBox();
        removeMushroomPos();

        List<int[][]> oldPosBoxses = new ArrayList<>(posBoxses);

        posBoxses.clear();
        moveBoxPos.clear();

        long nbBox = goalInGrid.get() - boxOnGoal.get();
        for (int w = 0; w < nbBox; ++w) {
            int[][] newPos = determOfBoxPos();
            int newX = newPos[0][0];
            int newY = newPos[0][1];

            addBox(newX, newY);

            int[][] boxCoordinates = {{newX, newY}};
            posBoxses.add(boxCoordinates);

            if (!oldPosBoxses.isEmpty() && w < oldPosBoxses.size()) {
                int oldX = oldPosBoxses.get(w)[0][0];
                int oldY = oldPosBoxses.get(w)[0][1];
                moveBoxPos.addAll(Arrays.asList(newX, newY, oldX, oldY));
            }
        }
    }


    void redoBox(List<int[][]> exPosBox) {
        clearBox();
        for (int[][] boxCoords : exPosBox) {
            for (int[] coord : boxCoords) {
                int key = coord[0];
                int value = coord[1];
                addBox(key, value);
            }
        }
    }

    void clearBox() {
        for (int i = 0; i < getHeight(); ++i) {
            for (int j = 0; j < getWidth(); ++j) {
                if(isBox(i,j)) {
                    matrix4Play[i][j].removeBox();
                }
            }
        }
    }

    private void addBox(int x, int y) {
        matrix4Play[x][y].addBox();
    }

    int[] getPosPlayer() {
        for (int i = 0; i < getHeight(); ++i) {
            for (int j = 0; j < getWidth(); ++j) {
                if (isPlayer(i, j)) {
                    return new int[]{i, j};
                }
            }
        }
        return null;
    }

     void moveMushroomTo(int row, int col) {
        posMush[0][0] = row;
        posMush[0][1] = col;
    }

    void storingPosBoxes() {
        for (int i = 0; i < getHeight(); ++i) {
            for (int j = 0; j < getWidth(); ++j) {
                if (isBox(i, j) || isBoxOnGoal(i, j)) {
                    int oldX = i;
                    int oldY = j;

                    int[][] newPos = determOfBoxPos();
                    int newX = newPos[0][0];
                    int newY = newPos[0][1];

                    // Add these values to the ObservableList
                    moveBoxPos.addAll(Arrays.asList(oldX, oldY, newX, newY));
                }
            }
        }
    }
}

