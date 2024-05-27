package sokoban.view;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.geometry.Insets;
import sokoban.viewmodel.GridViewModel4Design;

public class GridView4Design extends GridView {

    private static final int PADDING = 10;
    private GridViewModel4Design gridViewModel4Design;

    GridView4Design(GridViewModel4Design gridViewModel4Design, DoubleBinding gridWidth, DoubleBinding gridHeight) {
        // Pour visualiser les limites de la grille
//        setStyle("-fx-background-color: lightgrey");
        this.gridViewModel4Design = gridViewModel4Design;
        updateGrid(gridViewModel4Design,gridWidth,gridHeight);
        this.gridViewModel4Design.isPossible();
        this.gridViewModel4Design.playPosComputer();

    }

    private void updateGrid(GridViewModel4Design gridViewModel4Design, DoubleBinding gridWidth, DoubleBinding gridHeight) {

        minWidthProperty().bind(gridWidth);
        minHeightProperty().bind(gridHeight);

        int GRID_WIDTH = gridViewModel4Design.widthProperty().get();
        int GRID_HEIGHT = gridViewModel4Design.heightProperty().get();


        double gridRatio = (double) GRID_WIDTH / GRID_HEIGHT;

        DoubleBinding gridSize = Bindings.createDoubleBinding(
                () -> (widthProperty().get() - PADDING * 2) / (heightProperty().get() - PADDING * 2),
                widthProperty(), heightProperty());

        setGridLinesVisible(false);
        setPadding(new Insets(PADDING));

        DoubleBinding cellSize = (DoubleBinding) Bindings.when(gridSize.greaterThan(gridRatio))
                .then(heightProperty().subtract(PADDING * 2).divide(GRID_HEIGHT))
                .otherwise(widthProperty().subtract(PADDING * 2).divide(GRID_WIDTH));


        // Remplissage de la grille
        for (int i = 0; i < GRID_HEIGHT; ++i) {
            for (int j = 0; j < GRID_WIDTH; ++j) {
                CellView4Design cellView = new CellView4Design(gridViewModel4Design.getCellViewModel4Design(i, j), cellSize);
                add(cellView, j, i);
            }
        }


    }

}
