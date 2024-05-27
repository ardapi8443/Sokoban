package sokoban;

import javafx.application.Application;
import javafx.stage.Stage;
import sokoban.model.Board4Design;
import sokoban.model.Board4Play;
import sokoban.view.BoardView;
import sokoban.view.BoardView4Design;
import sokoban.view.BoardView4Play;
import sokoban.viewmodel.ArtefactViewModel;
import sokoban.viewmodel.BoardViewModel;
import sokoban.viewmodel.BoardViewModel4Design;
import sokoban.viewmodel.BoardViewModel4Play;

public class SokobanApp extends Application  {

    @Override
    public void start(Stage primaryStage) {
        Board4Design board4Design = new Board4Design(20,15,"");
        BoardViewModel4Design boardViewModel4Design = new BoardViewModel4Design(board4Design);
        BoardView4Design boardView4Design = new BoardView4Design(primaryStage,boardViewModel4Design);
    }

    public static void main(String[] args) {
        launch();
    }

}
