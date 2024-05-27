package sokoban.view;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.skin.TextInputControlSkin;
import javafx.scene.input.KeyCode;
import sokoban.viewmodel.GridViewModel4Play;

import java.util.ArrayList;
import java.util.List;

public class GridView4Play extends GridView {

    private static final int PADDING = 10;
    private CellView4Play cellView;

    private int GRID_HEIGHT;
    private int GRID_WIDTH;
    private CellView4Play[][] matrixView4Play;
    private ObservableList<Integer> moveBoxPos;
    private SimpleBooleanProperty mustSetLabel;
    private final GridViewModel4Play gridViewModel4Play;

//    int numbox = 1;

    GridView4Play(GridViewModel4Play gridViewModel, DoubleBinding gridWidth, DoubleBinding gridHeight) {
        // Pour visualiser les limites de la grille
        this.gridViewModel4Play = gridViewModel;
        moveBoxPos = gridViewModel4Play.moveBoxPosProperty();
        mustSetLabel = gridViewModel4Play.MustSetLabelProperty();
        updateGrid(gridViewModel,gridWidth,gridHeight);
        configureKeyEvents(gridViewModel);
        setListener();
    }

    private void configureKeyEvents(GridViewModel4Play gridViewModel4Play) {

        setFocusTraversable(true);  // Permet à GridView de recevoir le focus clavier

        setOnKeyPressed(event -> {

            if (event.isControlDown() && event.getCode() == KeyCode.Z) {
                gridViewModel4Play.undo();
                event.consume();
            } else if (event.isControlDown() && event.getCode() == KeyCode.Y) {
                gridViewModel4Play.redo();
                event.consume();
            } else {
                switch (event.getCode()) {
                    case Z, UP -> gridViewModel4Play.movePlayer(TextInputControlSkin.Direction.UP);
                    case S, DOWN -> gridViewModel4Play.movePlayer(TextInputControlSkin.Direction.DOWN);
                    case Q, LEFT -> gridViewModel4Play.movePlayer(TextInputControlSkin.Direction.LEFT);
                    case D, RIGHT -> gridViewModel4Play.movePlayer(TextInputControlSkin.Direction.RIGHT);

                }
                event.consume(); // Consommer l'événement
            }
        });
    }

    private void updateGrid(GridViewModel4Play gridViewModel4Play, DoubleBinding gridWidth, DoubleBinding gridHeight) {

        minWidthProperty().bind(gridWidth);
        minHeightProperty().bind(gridHeight);

        this.GRID_WIDTH = gridViewModel4Play.widthProperty().get();
        this.GRID_HEIGHT = gridViewModel4Play.heightProperty().get();

        double gridRatio = (double) GRID_WIDTH / GRID_HEIGHT;

        DoubleBinding gridSize = Bindings.createDoubleBinding(
                () -> (widthProperty().get() - PADDING * 2) / (heightProperty().get() - PADDING * 2),
                widthProperty(), heightProperty());

        setGridLinesVisible(true);
        setPadding(new Insets(PADDING));

        DoubleBinding cellSize = (DoubleBinding) Bindings.when(gridSize.greaterThan(gridRatio))
                .then(heightProperty().subtract(PADDING * 2).divide(GRID_HEIGHT))
                .otherwise(widthProperty().subtract(PADDING * 2).divide(GRID_WIDTH));


        this.matrixView4Play = new CellView4Play[GRID_HEIGHT][GRID_WIDTH];
            // Remplissage de la grille
            for (int i = 0; i < GRID_HEIGHT; ++i) {
                matrixView4Play[i] = new CellView4Play[GRID_WIDTH];
                for (int j = 0; j < GRID_WIDTH; ++j) {
                    cellView = new CellView4Play(gridViewModel4Play.getCellViewModel4Play(i, j), cellSize);
//                    cellView.setVal(++numbox);
                    matrixView4Play[i][j] = cellView;
                    add(cellView, j, i);
                }
            }
    }

        private void setListener(){

            moveBoxPos.addListener((ListChangeListener<Integer>) change -> {
                while (change.next()) {
                    if (change.wasAdded()) {
                        List<? extends Integer> addedSubList = change.getAddedSubList();

                        for (int i = 0; i < addedSubList.size(); i += 4) {
                            if (i + 3 < addedSubList.size()) {
                                int newLine = addedSubList.get(i);
                                int newCol = addedSubList.get(i + 1);
                                int oldLine = addedSubList.get(i + 2);
                                int oldCol = addedSubList.get(i + 3);

                                int oldVal = matrixView4Play[oldLine][oldCol].val;
                                matrixView4Play[newLine][newCol].setVal(oldVal);
                            }
                        }
                    }
                }
            });

            mustSetLabel.addListener(change -> {
                if(mustSetLabel.get()) {
                    for (int i = 0; i < GRID_HEIGHT; ++i) {
                        for (int j = 0; j < GRID_WIDTH; ++j) {
                                matrixView4Play[i][j].setLabel();
                        }
                    }
                }
            });

        }

        void removeKeyBindings(){
            setOnKeyPressed(event -> {

                if (event.isControlDown() && event.getCode() == KeyCode.Z) {
                    event.consume();
                } else if (event.isControlDown() && event.getCode() == KeyCode.Y) {
                    event.consume();
                } else {
                    switch (event.getCode()) {
                        case Z, UP, Q, LEFT,S, DOWN, D, RIGHT  -> {
                        }
                    }
                    event.consume();
                }
            });
        }

    }
