package sokoban.view;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.SetChangeListener;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import sokoban.model.Artefact;
import sokoban.viewmodel.CellViewModel4Design;

public class CellView4Design extends CellView {
    final CellViewModel4Design viewModel;
    private final BooleanProperty startPlay = new SimpleBooleanProperty(false);

    CellView4Design (CellViewModel4Design cellViewModel, DoubleBinding cellWidthProperty) {
        super(cellWidthProperty);
        this.viewModel = cellViewModel;

        setHoverEffect();
        updateImages();
        layoutControls();
        configureBindings();
        configureBindings4Design();
        mouseActions();
    }

    private void configureBindings4Design() {
        startPlay.bind(viewModel.readyToPlayProperty());
    }

    private void setHoverEffect() {
        ColorAdjust colorAdjust = new ColorAdjust();
        setEffect(colorAdjust);

        setOnMouseEntered(event -> {
            setOpacity(0.7);
            colorAdjust.setBrightness(-0.5);
            colorAdjust.setContrast(0.5);
        });

        setOnMouseExited(event -> {
            setOpacity(1.0);
            colorAdjust.setBrightness(0.0);
            colorAdjust.setContrast(0.0);
        });
    }

    void layoutControls() {
        for (Image image : getImages()) {
            ImageView imageView = new ImageView(image);
            imageView.setPreserveRatio(true);
            imageView.fitWidthProperty().bind(widthProperty.multiply(viewModel.scaleProperty()));
            getChildren().add(imageView);
        }
    }
    void configureBindings() {
        minWidthProperty().bind(widthProperty);
        maxHeightProperty().bind(widthProperty);

        viewModel.getArtefacts().addListener((SetChangeListener<Artefact>) change -> updateImages());

        getImages().addListener((ListChangeListener<Image>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    for (Image image : change.getAddedSubList()) {
                        ImageView imageView1 = new ImageView(image);
                        imageView1.setPreserveRatio(true);
                        imageView1.fitWidthProperty().bind(widthProperty.multiply(viewModel.scaleProperty()));
                        getChildren().add(imageView1);
                    }
                }
            }
        });
    }

    void updateImages() {
        images.clear();

        for (Artefact artefact : viewModel.getArtefacts()) {
            switch (artefact.getArtefactMode()) {
                case GROUND -> images.add(new Image("ground.png"));
                case BOX -> images.add(new Image("box.png"));
                case PLAYER -> images.add(new Image("player.png"));
                case WALL -> images.add(new Image("wall.png"));
                case GOAL -> images.add(new Image("goal.png"));

            }
        }
    }

    @Override
    void mouseActions() {
        setOnDragDetected(event -> {
            if (event.getButton() == MouseButton.PRIMARY || event.getButton() == MouseButton.SECONDARY)
                startFullDrag();
            event.consume();
        });
        // remove les artefacts de la cellule
        setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.SECONDARY) {
                // devrait retirer le dernier atefact ? mais impossible dans un Set . .
                viewModel.removeLastArtefact();
            }
        });

        // Gérer le glissement sur les cellules
        setOnMouseDragEntered(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                viewModel.updateCellSet();
                event.consume();
            } else if (event.getButton() == MouseButton.SECONDARY) {
                viewModel.clear();
                event.consume();
            }
        });
        // un clic sur la cellule permet de jouer celle-ci
        setOnMousePressed(e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                viewModel.updateCellSet();
            }
        });

        setOnMouseDragged(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                viewModel.updateCellSet();
                event.consume();
            }
        });
    }

}
