package sokoban.view;

import javafx.geometry.Insets;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import sokoban.model.*;
import sokoban.viewmodel.ArtefactViewModel;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.util.Arrays;
import java.util.List;

import static sokoban.model.ArtefactMode.*;


public class ArtefactView extends VBox {

    private ImageView selectedImageView = null;


    public ArtefactView(ArtefactViewModel artefactViewModel) {
        List<String> imageUrls = Arrays.asList(
                "ground.png", "box.png", "player.png", "wall.png", "goal.png"
        );

        for (String imageUrl : imageUrls) {
            ImageView imageView = createImageView(imageUrl, artefactViewModel);
            setSpacing(15);
            getChildren().add(imageView);
        }

        setPadding(new Insets(10));
//        setStyle("-fx-background-color: lightgrey");
    }

    private ImageView createImageView(String imageUrl, ArtefactViewModel artefactViewModel) {
        Image image = new Image(imageUrl);
        ImageView imageView = new ImageView(image);

        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.BLUE);
        dropShadow.setRadius(5);
        dropShadow.setOffsetX(2);
        dropShadow.setOffsetY(6);
        // set par défaut l'image courant à ground ;
      //  artefactViewModel.setCurrentArtefact("ground");

        imageView.setOnMouseEntered(e -> imageView.setEffect(dropShadow));
        imageView.setOnMouseExited(e -> {
            if (selectedImageView != imageView) {
                imageView.setEffect(null);
            }
        });

        imageView.setOnMouseClicked(e -> {
            // Retire l'effet de l'ancienne image sélectionnée, si elle existe
            if (selectedImageView != null && selectedImageView != imageView) {
                selectedImageView.setEffect(null);
                selectedImageView.getStyleClass().remove("selected");
            }

            // Appliquer l'effet sur la nouvelle image sélectionnée
            selectedImageView = imageView;
            selectedImageView.setEffect(dropShadow);
            selectedImageView.getStyleClass().add("selected");

            switch (imageUrl) {
                case "ground.png" -> artefactViewModel.setCurrentArtefact(GROUND);
                case "box.png" -> artefactViewModel.setCurrentArtefact(BOX);
                case "player.png" -> artefactViewModel.setCurrentArtefact(PLAYER);
                case "wall.png" -> artefactViewModel.setCurrentArtefact(WALL);
                case "goal.png" -> artefactViewModel.setCurrentArtefact(GOAL);
            }
        });
        return imageView;
    }

}