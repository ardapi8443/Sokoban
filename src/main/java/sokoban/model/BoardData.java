package sokoban.model;

import javafx.stage.FileChooser;

import java.io.*;

public class BoardData {
    private final   String content;
    private final int width;
    private final int height;

    private static  int CURRENT_LEVEL = 1;
    private final FileChooser fileChooser = new FileChooser();


    public BoardData(String content, int width, int height) {
        this.content = content;
        this.width = width;
        this.height = height;
    }
    public BoardData() {
        this.content = "";
        this.width = 0;
        this.height = 0;
    }

    // Getters
    public String getContent() {
        return content;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public BoardData readBoardFromFile() {


        File selectedFile = fileChooser.showOpenDialog(null);

        StringBuilder contentBuilder = new StringBuilder();
        int width = 0;
        int height = 0;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(selectedFile));
            String line;
            while ((line = reader.readLine()) != null) {
                height++;
                if (line.length() > width) {
                    width = line.length();
                }
                contentBuilder.append(line).append("\n");
            }


        } catch (NullPointerException | IOException e){
            System.out.println(e.getMessage());
        }

        String content = contentBuilder.toString();

        return new BoardData(content, width, height);
    }

    public BoardData loadLevel(Integer levelNumber) {
        File levelFile;

        if (levelNumber != null) {
            String levelFilePath = "boards/level" + CURRENT_LEVEL + ".xsb";
             levelFile = new File(levelFilePath);
             CURRENT_LEVEL++;
        } else {
             levelFile = fileChooser.showOpenDialog(null);
        }

        // Construire le chemin du fichier de niveau en fonction de currentLevel


        StringBuilder contentBuilder = new StringBuilder();
        int width = 0;
        int height = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(levelFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                height++;
                if (line.length() > width) {
                    width = line.length();
                }
                contentBuilder.append(line).append("\n");
            }
        } catch (FileNotFoundException e) {
            System.out.println("Fichier de niveau non trouvé : " + levelFile);

        } catch (IOException e) {
            System.out.println("Erreur lors de la lecture du fichier de niveau : " + e.getMessage());

        }

        String content = contentBuilder.toString();
        return new BoardData(content, width, height);

    }

}
