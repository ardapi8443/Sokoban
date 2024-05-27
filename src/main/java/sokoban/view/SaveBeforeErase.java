package sokoban.view;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class SaveBeforeErase extends Alert {

//    private final Label  areYouSure = new Label("do you want to save before erasing this game ?");

    public SaveBeforeErase(AlertType alertType) {
        super(alertType);
        setTitle("Confirmation Dialog");
        setHeaderText("youre board have been modified !");
        setContentText("do you want to save your changes ?");

        ButtonType btnNo = new ButtonType("No");

        getButtonTypes().add(btnNo);
    }

}
