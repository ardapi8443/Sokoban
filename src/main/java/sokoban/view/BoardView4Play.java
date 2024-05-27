package sokoban.view;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.StringBinding;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sokoban.model.Board4Design;
import sokoban.viewmodel.BoardViewModel4Design;
import sokoban.viewmodel.BoardViewModel4Play;


public class BoardView4Play extends BoardView {
    private final BoardViewModel4Play boardViewModel4Play;
    private final Button finishButton = new Button("Finish");
    private final Button mushButton = new Button("Show mushroom");
    private final Button hideMushButton = new Button("Hide mushroom");
    private final VBox errorContainer = new VBox();
    private final Label scoreBoard = new Label("Score");
    private final Label numberOfMove = new Label("");
    private final Label numberOfGoalReached = new Label("");
    private final Label victory = new Label("");
    private final Label boxOnGoal = new Label("Met(s) la/les caisse(s) sur le(s) goal pour gagner la partie");

    private GridView4Play gridView4Play;

    private final Stage primaryStage;
    public BoardView4Play(Stage primaryStage, BoardViewModel4Play boardViewModel4Play) {
        bottomContainer = new HBox();
        this.primaryStage = primaryStage;
        this.boardViewModel4Play = boardViewModel4Play;
        start(primaryStage);
        createDesignZone();
        boardViewModel4Play.saveGrid();

    }

     void createHeader() {
        numberOfMove.textProperty().bind(boardViewModel4Play.getMovesProperty()
                .asString("Number of moves played: %d" ));


        StringBinding scoreText = Bindings.createStringBinding(() ->
                        String.format("Number of goals reached : %d of %d",
                                boardViewModel4Play.boxOnGoal().get(),
                                boardViewModel4Play.nbGoalProperty().get()),
                boardViewModel4Play.nbGoalProperty(),
                boardViewModel4Play.boxOnGoal());

        numberOfGoalReached.textProperty().bind(scoreText);

        victory.textProperty().bind(boardViewModel4Play.getMovesProperty().asString("You Won in %d moves, congratulations !!"));

    }
    void createGrid() {

        DoubleBinding gridWidth = Bindings.createDoubleBinding(
                () -> {
                    var size = Math.min(widthProperty().get(), heightProperty().get() - topContainer.heightProperty().get());
                    return Math.floor(size / getGridWidth()) * getGridWidth();
                },
                widthProperty(),
                heightProperty(),
                topContainer.heightProperty(),
                bottomContainer.heightProperty());

        DoubleBinding gridHeight = Bindings.createDoubleBinding(
                () -> {
                    var size = Math.min(widthProperty().get(),
                            heightProperty().get() - topContainer.heightProperty().get() - bottomContainer.heightProperty().get());
                    return Math.floor(size / getGridHeight()) * getGridHeight();
                },
                widthProperty(),
                heightProperty(),
                topContainer.heightProperty(),
                bottomContainer.heightProperty());


        gridView4Play = new GridView4Play(boardViewModel4Play.getGridViewModel4Play(), gridWidth, gridHeight);

        gridView4Play.minHeightProperty().bind(gridHeight);
        gridView4Play.minWidthProperty().bind(gridWidth);
        gridView4Play.maxHeightProperty().bind(gridHeight);
        gridView4Play.maxWidthProperty().bind(gridWidth);

        setCenter(gridView4Play);


    }
    void configureBindings() {

        victory.visibleProperty().bind(boardViewModel4Play.boxOnGoalProperty());
        victory.managedProperty().bind(victory.visibleProperty());


        gridWidth.bind(boardViewModel4Play.getWithProperty());
        gridHeight.bind(boardViewModel4Play.getHeightProperty());

        boxOnGoal.visibleProperty().bind(boardViewModel4Play.boxOnGoalProperty().not());
        boxOnGoal.managedProperty().bind(boxOnGoal.visibleProperty());

        mushButton.visibleProperty().bind(boardViewModel4Play.boxOnGoalProperty().not());

        boardViewModel4Play.boxOnGoalProperty().addListener((observable, oldValue, newValue) -> {
            if (Boolean.TRUE.equals(newValue)) {
                requestFocus();
            }
        });

    }
    void layoutControls() {

        scoreBoard.getStyleClass().add("header");
        victory.getStyleClass().add("header");
        headerBox.getChildren().addAll(scoreBoard,numberOfGoalReached);
        headerBox.setAlignment(Pos.TOP_CENTER);
        topContainer.setAlignment(Pos.TOP_CENTER);

        errorMessages.addAll(numberOfMove,numberOfGoalReached,victory,boxOnGoal);

        errorContainer.getChildren().addAll(errorMessages);
        errorContainer.setAlignment(Pos.TOP_CENTER);

        topContainer.getChildren().addAll(headerBox,errorContainer);

        setTop(topContainer);

        bottomContainer.setAlignment(Pos.BOTTOM_CENTER);
        setBottom(bottomContainer);

    }

    private void createDesignZone() {
        bottomContainer.getChildren().addAll(finishButton,mushButton);
        bottomContainer.setAlignment(Pos.CENTER);
        bottomContainer.setMinHeight(100);

        bottomContainer.setVisible(true);
        bottomContainer.setManaged(true);

        setBottom(bottomContainer);


        finishButton.setOnAction((e) -> {

            CellView4Play.Reset();
            Board4Design board4Design = new Board4Design(boardViewModel4Play.getWithProperty().get(),boardViewModel4Play.getHeightProperty().get(),boardViewModel4Play.getSavedGrid());
            BoardViewModel4Design boardViewModel4Design = new BoardViewModel4Design(board4Design);
            new BoardView4Design(primaryStage,boardViewModel4Design);


        });

        mushButton.setOnAction((e) -> {
            bottomContainer.getChildren().remove(mushButton);
            bottomContainer.getChildren().add(hideMushButton);

            boardViewModel4Play.initMushroomPos();

            gridView4Play.setDisable(true);
            hideMushButton.requestFocus();
        });

        hideMushButton.setOnAction((e) -> {
            bottomContainer.getChildren().remove(hideMushButton);
            bottomContainer.getChildren().add(mushButton);
            gridView4Play.setDisable(false);

            boardViewModel4Play.hideMushroom();
        });
    }

    protected void checkIfGameOver(){
        if(boardViewModel4Play.boxOnGoalProperty().get()){
            gridView4Play.removeKeyBindings();
        }
    }
}




