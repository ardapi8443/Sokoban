package sokoban.view;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sokoban.model.Board;
import sokoban.model.Board4Design;
import sokoban.model.Board4Play;
import sokoban.viewmodel.BoardViewModel4Design;
import sokoban.viewmodel.BoardViewModel4Play;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BoardView4Design extends BoardView {
    private final BoardViewModel4Design boardViewModel4Design;
    private final ArtefactView artefactView;
    private final Button playButton = new Button("play");
    private final MenuBar menu = new MenuBar();
    private final VBox errorContainer = new VBox();
    private final Label errorMessage = new Label("Please correct the following error(s):");
    private final Label boxCount = new Label("At least one box is required");
    private final Label goalCount = new Label("At least one target is required");
    private final Label matchingGoalAndBox = new Label("The number of box and goal must match");
    private final Label isPossible = new Label("This game is impossible to play");
    private final Label headerLabel = new Label("");
    private final Label playerCount = new Label("");
    private BooleanProperty haveChanged = new SimpleBooleanProperty();
    private GridView4Design gridView4Design;
    private final ObservableList<Label> errorMessages = FXCollections.observableArrayList();
    private final Stage primaryStage;

    public BoardView4Design(Stage primaryStage, BoardViewModel4Design boardViewModel4Design) {
        bottomContainer = new HBox();
        this.boardViewModel4Design = boardViewModel4Design;
        artefactView = new ArtefactView(boardViewModel4Design.getArtefactViewModel());
        this.primaryStage = primaryStage;

        start(primaryStage);

        setUpdateCellBehavior(primaryStage);
        createArtefact();
        createMenu();
        createPlayZone();


    }

    private void setUpdateCellBehavior(Stage stage) {
        haveChanged.addListener((e, oldVal, newVal) -> {
            if (newVal) {
                stage.setTitle("*" + stage.getTitle());
                System.out.println(haveChanged.get());
            } else if (!newVal) {
                stage.setTitle(TITLE);
            }
        });

    }

    private void createArtefact(){
        setLeft(artefactView);
        setAlignment(artefactView, Pos.CENTER);
    }

    private void createMenu() {
        Menu menuFile = new Menu("File");

        MenuItem nnew = new MenuItem("New");
        MenuItem open = new MenuItem("Open");
        MenuItem save = new MenuItem("Save");
        MenuItem exit = new MenuItem("Exit");
        MenuItem dom = new MenuItem("DOOM");

        handleMenuEvent(nnew, open, save, exit,dom);

        menuFile.getItems().addAll(nnew, open, save, exit,dom);

        menu.getMenus().add(menuFile);
    }

    public void createHeader() {
            StringBinding headerText = Bindings.createStringBinding(() ->
                            String.format("Number of filled cells: %d of %d",
                                    boardViewModel4Design.filledCellsCountProperty().get(),
                                    boardViewModel4Design.maxFilledCells().get()),
                    boardViewModel4Design.filledCellsCountProperty(),
                    boardViewModel4Design.maxFilledCells());

            headerLabel.textProperty().bind(headerText);
            playerCount.textProperty().bind(boardViewModel4Design.playerInProperty()
                    .asString("A player is required %d : " + boardViewModel4Design.maxPlayer()));
    }

    private void handleMenuEvent(MenuItem nnew, MenuItem open, MenuItem save, MenuItem exit, MenuItem DOOM) {
        exit.setOnAction(e -> System.exit(0));

        save.setOnAction(e -> {
            boardViewModel4Design.save();
            boardViewModel4Design.haveChangedProperty().set(false);
        });

        open.setOnAction(e -> {
            if (haveChanged.get()) {
                SaveBeforeErase alert = new SaveBeforeErase(Alert.AlertType.CONFIRMATION);
                Optional<ButtonType> res = alert.showAndWait();
                if (res.isPresent() && res.get() == ButtonType.OK) {
                    boardViewModel4Design.save();
                    boardViewModel4Design.loadLevel(null);
                    reload();

                    boardViewModel4Design.haveChangedProperty().set(false);
                } else if (res.isPresent() && res.get() == ButtonType.CANCEL) {}
                else {
                    boardViewModel4Design.loadLevel(null);
                    reload();
                    boardViewModel4Design.haveChangedProperty().set(false);
                }
            } else {
                boardViewModel4Design.loadLevel(null);
                reload();
            }
        });


        DOOM.setOnAction(e -> {
            boardViewModel4Design.dom();
            boardViewModel4Design.haveChangedProperty().set(false);
        });

        nnew.setOnAction(e -> {
            if (haveChanged.get()) {
                SaveBeforeErase alert = new SaveBeforeErase(Alert.AlertType.CONFIRMATION);
                Optional<ButtonType> res = alert.showAndWait();
                if (res.isPresent() && res.get() == ButtonType.OK) {
                    boardViewModel4Design.save();
                    newAction();
                    boardViewModel4Design.haveChangedProperty().set(false);
                } else if(res.isPresent() && res.get() == ButtonType.CANCEL) {}
                else {
                    if (newAction()) {
                        boardViewModel4Design.haveChangedProperty().set(false);
                    }
                }
            }else {
                newAction();
            }
        });
    }

    private boolean newAction() {
        Dialog<List<Integer>> dialog = new NewDialog(new ArrayList<>());
        Optional<List<Integer>> result = dialog.showAndWait();
        if (result.isPresent()) {
            int width = result.get().get(0);
            int height = result.get().get(1);

            boardViewModel4Design.nnew(width, height);
            reload();
            return true;
        }
        return false;
    }
    private void reload() {
        createGrid();
        createHeader();
        configureBindings();
    }
    void createGrid() {

        DoubleBinding gridWidth = Bindings.createDoubleBinding(
                () -> {
                    var size = Math.min(widthProperty().get() - artefactView.widthProperty().get(),
                            heightProperty().get() - topContainer.heightProperty().get());
                    return Math.floor(size / getGridWidth()) * getGridWidth();
                },
                widthProperty(),
                heightProperty(),
                topContainer.heightProperty(),
                bottomContainer.heightProperty());

        DoubleBinding gridHeight = Bindings.createDoubleBinding(
                () -> {
                    var size = Math.min(widthProperty().get() - artefactView.widthProperty().get(),
                            heightProperty().get() - topContainer.heightProperty().get() - bottomContainer.heightProperty().get());
                    return Math.floor(size / getGridHeight()) * getGridHeight();
                },
                widthProperty(),
                heightProperty(),
                topContainer.heightProperty(),
                bottomContainer.heightProperty());

            gridView4Design = new GridView4Design(boardViewModel4Design.getGridViewModel4Design(), gridWidth, gridHeight);

            gridView4Design.minHeightProperty().bind(gridHeight);
            gridView4Design.minWidthProperty().bind(gridWidth);
            gridView4Design.maxHeightProperty().bind(gridHeight);
            gridView4Design.maxWidthProperty().bind(gridWidth);

        setCenter(gridView4Design);
    }

    void configureBindings() {

        playerCount.visibleProperty().bind(boardViewModel4Design.playerProperty().not());
        playerCount.managedProperty().bind(playerCount.visibleProperty());

        matchingGoalAndBox.visibleProperty().bind(boardViewModel4Design.matchGoalAndBox().not());
        matchingGoalAndBox.managedProperty().bind(matchingGoalAndBox.visibleProperty());

        goalCount.visibleProperty().bind(boardViewModel4Design.goalInProperty().not());
        goalCount.managedProperty().bind(goalCount.visibleProperty());

        boxCount.visibleProperty().bind(boardViewModel4Design.boxInProperty().not());
        boxCount.managedProperty().bind(boxCount.visibleProperty());

        isPossible.visibleProperty().bind(boardViewModel4Design.getIsPossibleProperty());
        isPossible.managedProperty().bind(isPossible.visibleProperty());

        errorMessage.visibleProperty().bind(boardViewModel4Design.readyToPlayProperty().not());
        errorMessage.managedProperty().bind(errorMessage.visibleProperty());

        haveChanged.bind(boardViewModel4Design.haveChangedProperty());
//        artefactView.visibleProperty().bind(boardViewModel.readyToPlayProperty().not());
//        artefactView.managedProperty().bind(artefactView.visibleProperty());

        gridWidth.bind(boardViewModel4Design.getWithProperty());
        gridHeight.bind(boardViewModel4Design.getHeightProperty());

        playButton.disableProperty().bind(boardViewModel4Design.readyToPlayProperty().not());

//        boxOnGoal.visibleProperty().bind(boardViewModel.is4DesignProperty().not());
//        boxOnGoal.managedProperty().bind(boxOnGoal.visibleProperty());

    }

    private void createPlayZone() {
        bottomContainer.getChildren().add(playButton);
        bottomContainer.setAlignment(Pos.CENTER);
        bottomContainer.setMinHeight(100);
        setBottom(bottomContainer);

        playButton.setOnAction((e) -> {
            //doit sauver le grid
            boardViewModel4Design.saveGrid();

            SaveBeforeErase alert = new SaveBeforeErase(Alert.AlertType.CONFIRMATION);
            Optional<ButtonType> res = alert.showAndWait();

            if (res.isPresent() && res.get() == ButtonType.OK) {
                boardViewModel4Design.save();
            }
            else if (res.isPresent() && res.get() != ButtonType.CANCEL) {
                Board4Play board4Play = new Board4Play(boardViewModel4Design.getWithProperty().get(), boardViewModel4Design.getHeightProperty().get(), boardViewModel4Design.getSavedGrid());
                BoardViewModel4Play boardViewModel4Play = new BoardViewModel4Play(board4Play);
                new BoardView4Play(primaryStage, boardViewModel4Play);
            }
        });
    }

    void layoutControls() {

        headerLabel.getStyleClass().add("header");

        headerBox.getChildren().add(headerLabel);
        headerBox.setAlignment(Pos.TOP_CENTER);

        errorMessages.addAll(errorMessage,playerCount,goalCount,boxCount,matchingGoalAndBox, isPossible);

        errorContainer.getChildren().addAll(errorMessages);
        topContainer.getChildren().addAll(menu,headerBox,errorContainer);

        errorContainer.setAlignment(Pos.TOP_CENTER);
        setTop(topContainer);

        for (Label lbl : errorMessages)
            lbl.getStyleClass().add("errors");

    }

    @Override
    void checkIfGameOver() {

    }
}
