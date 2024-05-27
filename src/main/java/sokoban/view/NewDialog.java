package sokoban.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.converter.IntegerStringConverter;

import java.util.ArrayList;
import java.util.List;

public class NewDialog extends Dialog<List<Integer>> {

    private List<Integer> result;

    private final static Label widthLabel = new Label("width");
    private final static TextField widthText = new TextField("10");
    private final static Label heightLabel = new Label("width");
    private final static TextField heightText = new TextField("10");
    private final static Label errWidth1 = new Label("Width must be at least 10");
    private final static Label errWidth2 = new Label("Width must be at most 50");
    private final static Label errHeight1 = new Label("Width must be at least 10");
    private final static Label errHeight2 = new Label("Width must be at most 50");
    private final static ButtonType btnTypeOK = ButtonType.OK;
    private static Button btnOK;
    private boolean isInvalidWidth1;
    private boolean isInvalidWidth2;
    private boolean isInvalidHeight1;
    private boolean isInvalidHeight2;

    public NewDialog(ArrayList<Integer> list) {
        super();
        result = list;
        result.add(10);
        result.add(10);

        setTitle("Create a new Grid");
        getDialogPane().setMinSize(300, 350);

        buildUI();
        setPropertyBindings();
        setResultConverter();
    }

    private void buildUI() {
        widthLabel.setPrefWidth(50);
        heightLabel.setPrefWidth(50);

        VBox content = new VBox();
        content.setPadding(new Insets(10));
        content.setAlignment(Pos.TOP_LEFT);
        content.setFillWidth(false);

        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);

        grid.add(widthLabel, 0, 0);
        grid.add(widthText, 1, 0);
        grid.add(errWidth1, 1, 1);
        grid.add(errWidth2, 1, 1);

        grid.add(heightLabel, 0, 2);
        grid.add(heightText, 1, 2);
        grid.add(errHeight1, 1, 3);
        grid.add(errHeight2, 1, 3);

        GridPane.setHgrow(widthLabel, Priority.ALWAYS);
        GridPane.setHgrow(heightLabel, Priority.ALWAYS);

        content.getChildren().add(grid);

        getDialogPane().setContent(content);
        getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, btnTypeOK);

        btnOK = (Button) getDialogPane().lookupButton(btnTypeOK);

        buildErrorMessages();
    }

    private void setPropertyBindings() {
        // Create TextFormatter instances with IntegerStringConverter for width and height
        TextFormatter<Integer> widthFormatter = new TextFormatter<>(new IntegerStringConverter(), 0);
        TextFormatter<Integer> heightFormatter = new TextFormatter<>(new IntegerStringConverter(), 0);

        // Bind the TextFormatter values to the corresponding SimpleIntegerProperty values
        widthText.setTextFormatter(widthFormatter);
        heightText.setTextFormatter(heightFormatter);

        // Add listeners to update the result list when TextFormatter values change
        widthFormatter.valueProperty().addListener((observable, oldValue, newValue) -> result.set(0, newValue));
        heightFormatter.valueProperty().addListener((observable, oldValue, newValue) -> result.set(1, newValue));

        try {
            widthText.setOnKeyTyped(e -> buildErrorMessages());
        } catch(NumberFormatException e){
            isInvalidWidth1 = false;
            widthText.setText("0");
        }

        try {
            heightText.setOnKeyTyped(e -> buildErrorMessages());
        } catch(NumberFormatException e) {
            isInvalidHeight1 = false;
            heightText.setText("0");
        }
    }


    private void setResultConverter() {
        setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                return result;
            }
            return null;
        });
    }

    private void buildErrorMessages() {
        String widthTextValue = widthText.getText();
        String heightTextValue = heightText.getText();

        int width = 0;
        try {
            width = Integer.parseInt(widthTextValue);
        } catch(NumberFormatException e) {
            isInvalidWidth1 = false;
            widthText.setText("0");
        }

        int height = 0;
        try {
            height = Integer.parseInt(heightTextValue);
        } catch(NumberFormatException e) {
            isInvalidHeight1 = false;
            heightText.setText("0");
        }

        isInvalidWidth1 = width < 10;
        isInvalidWidth2 = width > 50;
        isInvalidHeight1 = height < 10;
        isInvalidHeight2 = height > 50;

        errWidth1.setManaged(isInvalidWidth1);
        errWidth1.setVisible(isInvalidWidth1);
        errWidth1.setTextFill(Color.INDIANRED);

        errWidth2.setManaged(isInvalidWidth2);
        errWidth2.setVisible(isInvalidWidth2);
        errWidth2.setTextFill(Color.INDIANRED);

        errHeight1.setManaged(isInvalidHeight1);
        errHeight1.setVisible(isInvalidHeight1);
        errHeight1.setTextFill(Color.INDIANRED);

        errHeight2.setManaged(isInvalidHeight2);
        errHeight2.setVisible(isInvalidHeight2);
        errHeight2.setTextFill(Color.INDIANRED);

        btnOK.setDisable(isInvalidWidth1 || isInvalidWidth2 || isInvalidHeight1 || isInvalidHeight2);

    }

}