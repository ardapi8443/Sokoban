package sokoban.model;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.LongBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;

import java.util.Arrays;
import java.util.function.Predicate;

class Grid4Design extends Grid{
    private final BooleanProperty isPossible = new SimpleBooleanProperty(true);
    private  Cell4Design[][] matrix4Design;
    private final LongBinding boxInGrid;
    private final LongBinding filledCellsCount;
    private final LongBinding playerInGrid;
    private final ObservableList playpos = FXCollections.observableArrayList();

    public Grid4Design(int width, int height, String content) {
        super(width, height);

        matrix4Design = new Cell4Design[MAX_WIDTH_SIZE][MAX_HEIGHT_SIZE];
        for (int i = 0; i < MAX_WIDTH_SIZE; ++i) {
            matrix4Design[i] = new Cell4Design[MAX_HEIGHT_SIZE];
            for (int j = 0; j < MAX_HEIGHT_SIZE; ++j) {
                matrix4Design[i][j] = new Cell4Design();
            }
        }
        filledCellsCount = countElemInGrid(cell -> !cell.isEmpty());
        playerInGrid = countElemInGrid(Cell::containPlayer);
        boxInGrid = countElemInGrid(Cell::containBox);
        goalInGrid = countElemInGrid(Cell::containGoal);

        listener = change -> {
            isPossible();
        };

        if (!content.isEmpty()) {
            updateCellSetFromChars(content, width, height);
        }
    }

    public BooleanProperty getIsPossibleProperty() {
        return isPossible;
    }

    ObservableSet<Artefact> artefactSetProperty(int line, int col) {
        return matrix4Design[line][col].artefactSetProperty();
    }
    public ObservableSet<Artefact> getArtefacts(int line, int col) {
        return matrix4Design[line][col].getArtefactSet();
    }
    void invalidate() {
        filledCellsCount.invalidate();
        playerInGrid.invalidate();
        boxInGrid.invalidate();
        goalInGrid.invalidate();

    }
    void updateCellSet(int line, int col, Artefact updateCellSeterValue) {
        matrix4Design[line][col].addArtefact(updateCellSeterValue);
//        if (updateCellSeterValue.equals(new Player())) {
//            playPosComputer();
//            isPossible();
//        } else if (!playpos.isEmpty()) {
//            isPossible();
//        }
//
         invalidate();
    }
    public void clear(int line, int col) {
        matrix4Design[line][col].resetSet();
        invalidate();
    }
    public void removeLastArtefact(int line, int col) {
        matrix4Design[line][col].removeLastArtefact();
        invalidate();

        playpos.removeListener(listener);
        playpos.clear();
        playpos.addListener(listener);
    }
    private LongBinding countElemInGrid(Predicate<Cell4Design> filter) {
        return Bindings.createLongBinding(() ->
                Arrays.stream(matrix4Design)
                        .flatMap(Arrays::stream)
                        .filter(filter)
                        .count());
    }
    public void removePlayer(int line, int col, Artefact artefact) {
        if (containPlayerProperty(line, col)) {
            matrix4Design[line][col].removePlayer(artefact);
        }


        playpos.removeListener(listener);
        playpos.clear();
        playpos.addListener(listener);

    }
    public void removePlayer(Artefact artefact) {
        for (int i = 0; i < getHeight(); ++i) {
            for (int j = 0; j < getWidth(); ++j) {
                if (matrix4Design[i][j].containPlayer()) {
                    removePlayer(i, j, artefact);
                }
            }
        }
    }
    public void removeAllArtefact() {
        for (int i = 0; i < getHeight(); ++i) {
            for (int j = 0; j < getWidth(); ++j) {
                clear(i, j);
            }
        }
    }
    public String artefactsToChar() {
        String res = "";
        for (int i = 0; i < getHeight(); ++i) {
            for (int j = 0; j < getWidth(); ++j) {
                res += matrix4Design[i][j].readCell();
            }
            res += "\n";
        }
        return res;
    }
    public boolean containPlayerProperty(int line, int col) {
        return matrix4Design[line][col].containPlayer();
    }
    public LongBinding playerInGridProperty() {
        return playerInGrid;
    }
    public LongBinding filledCellsCountProperty() {
        return filledCellsCount;
    }
    public LongBinding boxInGridProperty() {
        return boxInGrid;
    }
    public LongBinding goalInGridProperty() {
        return goalInGrid;
    }
    public boolean isPlayer(int line, int col, Artefact artefact) {
        return matrix4Design[line][col].isPlayer(artefact);
    }
    public boolean isGround(int line, int col, Artefact artefact) {
        return matrix4Design[line][col].isGround(artefact);
    }

    public boolean isEmpty(int line, int col) {
        return matrix4Design[line][col].isEmpty();
    }
    public void updateCellSetFromChars(String res, int width, int height) {
        // supprime le contenu des artefacts avant de ré-injecter le contenu à l'ouverture d'un autre niveau
        // Diviser la chaîne en lignes
        String[] rows = res.split("\n");
        // Parcourir chaque ligne et chaque colonne de la grille

        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                char artefactChar = rows[i].charAt(j);
                matrix4Design[i][j].addArtefactFromChar(artefactChar);
            }
        }
    }

    public void updateCellSetFromChars(String res) {
        // supprime le contenu des artefacts avant de ré-injecter le contenu à l'ouverture d'un autre niveau
        // Diviser la chaîne en lignes
        String[] rows = res.split("\n");
        // Parcourir chaque ligne et chaque colonne de la grille
        for (int i = 0; i < getHeight(); ++i) {
            for (int j = 0; j < getWidth(); ++j) {
                char artefactChar = rows[i].charAt(j);
                matrix4Design[i][j].addArtefactFromChar(artefactChar);
            }
        }
    }


    public void nnew(int width, int height) {
        removeAllArtefact();
        setDimensions(width, height);
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                matrix4Design[i][j].resetSet();
            }
        }
    }
    void isPossible() {

        if(!playpos.isEmpty()){
            boolean resWall = recIsPossible((Integer) playpos.get(0), (Integer) playpos.get(1));
            boolean resBox = isBoxPossible((Integer) playpos.get(0), (Integer) playpos.get(1));

            isPossible.set(!resWall && !resBox);
        }

    }

    private boolean isBoxPossible(int i, int j) {
        boolean res;

        if (i == 0 && j == 0) {
            res = check_0_0();
        } else if (i == 0 && j == width.get() - 1) {
            res = check_0_max();
        } else if (i == height.get() - 1 && j == 0) {
            res = check_max_0();
        } else if (i == height.get() - 1 && j == width.get() - 1) {
            res = check_max_max();
        } else if (i == 0) {
            res = check_line_top((Integer) playpos.get(1));
        } else if (i == height.get() - 1) {
            res = check_line_bottom((Integer) playpos.get(1));
        } else if (j == 0) {
            res = check_line_left((Integer) playpos.get(0));
        } else if (j == width.get() - 1) {
            res = check_line_right((Integer) playpos.get(0));
        } else {
            res = check_any_other((Integer) playpos.get(0), (Integer) playpos.get(1));
        }
        return res;
    }
    private boolean recIsPossible(int i, int j) {

        boolean haveGoal = matrix4Design[i][j].containGoal();

        if (matrix4Design[i][j].containWall()) {
            return true;
        }
        matrix4Design[i][j].addArtefact(new Wall());

        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        for (int[] dir : directions) {
            int newRow = i + dir[0];
            int newCol = j + dir[1];

            if (newRow >= 0 && newRow < height.get() && newCol >= 0 && newCol < width.get()) {
                if (!matrix4Design[newRow][newCol].containWall()) {
                    matrix4Design[i][j].removeWall();
                    matrix4Design[i][j].addArtefact(new Player());
                    if(haveGoal){
                        matrix4Design[i][j].addArtefact(new Goal());
                    }
                    return false;
                }
                if (!recIsPossible(newRow, newCol)) {
                    matrix4Design[i][j].removeWall();
                    matrix4Design[i][j].addArtefact(new Player());
                    if(haveGoal){
                        matrix4Design[i][j].addArtefact(new Goal());
                    }
                    return false;
                }
            }
        }
        for (int[] dir : directions) {
            int newRow = i + dir[0];
            int newCol = j + dir[1];
            if (newRow >= 0 && newRow < height.get() && newCol >= 0 && newCol < width.get()) {
                if (!recIsPossible(newRow, newCol)) {
                    matrix4Design[i][j].removeWall();
                    matrix4Design[i][j].addArtefact(new Player());
                    if(haveGoal){
                        matrix4Design[i][j].addArtefact(new Goal());
                    }
                    return false;
                }
            }
        }

        matrix4Design[i][j].removeWall();
        matrix4Design[i][j].addArtefact(new Player());
        if(haveGoal){
            matrix4Design[i][j].addArtefact(new Goal());
        }

        // If all adjacent cells and their neighbors are walls, the player is trapped
        return true;
    }
    private boolean check_0_0() {
        Boolean below = checkBelow(0, 0);
        Boolean right = checkRight(0, 0);

        return below & right;

    }

    private boolean check_0_max() {
        Boolean below = checkBelow(0, width.get() - 1);
        Boolean left = checkLeft(0, width.get() - 1);

        return below && left;
    }

    private boolean check_max_0() {
        Boolean above = checkAbove(height.get() - 1, 0);
        Boolean right = checkRight(height.get() - 1, 0);

        return above && right;
    }

    private boolean check_max_max() {
        boolean above = checkAbove(height.get() - 1, width.get() - 1);
        boolean left = checkLeft(height.get() - 1, width.get() - 1);

        return above && left;
    }

    private boolean check_line_top(int j) {
        boolean left = checkLeft(0, j);
        boolean right = checkRight(0, j);
        boolean below = checkBelow(0, j);

        return left && right && below;
    }

    private boolean check_line_bottom(int j) {
        boolean left = checkLeft(height.get() - 1, j);
        boolean right = checkRight(height.get() - 1, j);
        boolean above = checkAbove(height.get() - 1, j);

        return left && right && above;
    }

    private boolean check_line_left(int i) {
        boolean right = checkRight(i, 0);
        boolean above = checkAbove(i, 0);
        boolean below = checkBelow(i, 0);

        return right && below && above;
    }

    private boolean check_line_right(int i) {
        boolean left = checkLeft(i, width.get() - 1);
        boolean above = checkAbove(i, width.get() - 1);
        boolean below = checkBelow(i, width.get() - 1);

        return left && below && above;
    }

    private boolean check_any_other(int i, int j) {
        boolean left = checkLeft(i, j);
        boolean right = checkRight(i, j);
        boolean above = checkAbove(i, j);
        boolean below = checkBelow(i, j);

        return left && below && above && right;
    }

    private boolean checkBelow(int i, int j) {
        return matrix4Design[i + 1][j].containBox() && matrix4Design[i + 2][j].containBox();
    }

    private boolean checkAbove(int i, int j) {
        return matrix4Design[i - 1][j].containBox() && matrix4Design[i - 2][j].containBox();
    }

    private boolean checkRight(int i, int j) {
        return matrix4Design[i][j + 1].containBox() && matrix4Design[i][j + 2].containBox();
    }

    private boolean checkLeft(int i, int j) {
        return matrix4Design[i][j - 1].containBox() && matrix4Design[i][j - 2].containBox();
    }

    void playPosComputer() {

        playpos.removeListener(listener);
        playpos.clear();
        playpos.addListener(listener);

        for (int i = 0; i < height.get(); i++) {
            for (int j = 0; j < width.get(); j++) {
                if (matrix4Design[i][j].containPlayer()) {
                    playpos.addAll(i, j);
                    break;

                }
            }
        }
    }


}
