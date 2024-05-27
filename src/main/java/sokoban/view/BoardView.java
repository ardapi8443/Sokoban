package sokoban.view;

import javafx.beans.binding.StringBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import sokoban.viewmodel.BoardViewModel;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import sokoban.viewmodel.GridViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


public abstract class BoardView extends BorderPane {

    IntegerProperty gridWidth = new SimpleIntegerProperty();
    IntegerProperty gridHeight = new SimpleIntegerProperty();
    private static final int SCENE_MIN_WIDTH = 500;
    private static final int SCENE_MIN_HEIGHT = 500;
    static final String TITLE = "Sokoban";
    final ObservableList<Label> errorMessages = FXCollections.observableArrayList();
    final VBox errorContainer = new VBox();
    final VBox topContainer = new VBox();
    final HBox headerBox = new HBox();

    HBox bottomContainer;


    void start(Stage stage) {
        configMainComponents(stage);
        Scene scene = new Scene(this, SCENE_MIN_WIDTH, SCENE_MIN_HEIGHT);
        String cssFile = Objects.requireNonNull(getClass().getResource("/styles.css")).toExternalForm();
        scene.getStylesheets().add(cssFile);
        stage.setScene(scene);
        stage.show();
        stage.setMinHeight(700);
        stage.setMinWidth(850);
    }

    private void configMainComponents(Stage stage) {
        stage.setTitle(TITLE);
        createHeader();
        configureBindings();
        createGrid();
        layoutControls();
        checkIfGameOver();
    }


    abstract void  createGrid();
    abstract void  createHeader();
    abstract void configureBindings();
    abstract void layoutControls();
    abstract void  checkIfGameOver();

    public int getGridWidth() {
        return gridWidth.get();
    }

    public int getGridHeight() {
        return gridHeight.get();
    }


}

