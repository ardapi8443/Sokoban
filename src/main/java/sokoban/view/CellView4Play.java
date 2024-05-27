package sokoban.view;

import javafx.beans.binding.DoubleBinding;
import javafx.collections.ListChangeListener;
import javafx.collections.SetChangeListener;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import sokoban.model.Artefact;
import sokoban.viewmodel.CellViewModel4Play;

import java.util.HashMap;
import java.util.Map;


public class CellView4Play extends CellView {
    final CellViewModel4Play viewModel;

    final Map<Image, String> imagePaths = new HashMap<>();
    static int numBox = 1;
    int val;

    CellView4Play (CellViewModel4Play cellViewModel, DoubleBinding cellWidthProperty) {
        super(cellWidthProperty);
        viewModel = cellViewModel;

        updateImages();
        layoutControls();
        configureBindings();
        mouseActions();
    }
    void layoutControls() {
        for (Image image : getImages()) {
            ImageView imageView = new ImageView(image);
            imageView.setPreserveRatio(true);
            imageView.fitWidthProperty().bind(widthProperty.multiply(viewModel.scaleProperty()));
            String imagePath = imagePaths.get(image);

            if ("box.png".equals(imagePath)) {
                val = numBox++;
                Label newTagBox = new Label("" + val);
                newTagBox.setStyle("-fx-background-color: white; -fx-padding: 1; -fx-border-color: black; -fx-border-width: 0.1;");
                getChildren().addAll(imageView, newTagBox);
             } else
                getChildren().addAll(imageView);
            }
        }

    void configureBindings() {
        minWidthProperty().bind(widthProperty);
        maxHeightProperty().bind(widthProperty);

        viewModel.getArtefacts().addListener((SetChangeListener<Artefact>) change -> updateImages());

        //quand deleteBox => save dans gridVM
        //quand addBox =>

        getImages().addListener((ListChangeListener<Image>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    for (Image image : change.getAddedSubList()) {
                        ImageView imageView1 = new ImageView(image);
                        imageView1.setPreserveRatio(true);
                        imageView1.fitWidthProperty().bind(widthProperty.multiply(viewModel.scaleProperty()));
                        getChildren().addAll(imageView1);
                    }
                }
            }
        });
    }

    @Override
    void updateImages() {
        images.clear();
        imagePaths.clear();

        for (Artefact artefact : viewModel.getArtefacts()) {
            Image image;
            String imagePath = "";
            switch (artefact.getArtefactMode()) {
                case GROUND -> imagePath = "ground.png";
                case BOX ->  imagePath = "box.png";
                case PLAYER -> imagePath = "player.png";
                case WALL -> imagePath = "wall.png";
                case GOAL -> imagePath = "goal.png";
                case MUSHROOM -> imagePath = "mushroom.png";
            }
            if (!imagePath.isEmpty()) {
                image = new Image(imagePath);
                images.add(image);
                imagePaths.put(image, imagePath);
            }
        }
    }

    @Override
    void mouseActions() {
        setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                if (viewModel.checkIfMushroom()) {
                    viewModel.exchangePosBox();
                }
            }
        });
    }

    void setVal(int val) {

            this.val = val;
            Label newTagBox = new Label("" + val);
            newTagBox.setStyle("-fx-background-color: white; -fx-padding: 1; -fx-border-color: black; -fx-border-width: 0.1;");
            getChildren().add(newTagBox);
    }

    void setLabel() {
        for (Image image : getImages()) {
            String imagePath = imagePaths.get(image);

            if ("box.png".equals(imagePath)) {
                Label newTagBox = new Label("" + val);
                newTagBox.setStyle("-fx-background-color: white; -fx-padding: 1; -fx-border-color: black; -fx-border-width: 0.1;");
                getChildren().add(newTagBox);
            }
        }
    }

    public static void Reset() {
            numBox = 1;
    }
}
