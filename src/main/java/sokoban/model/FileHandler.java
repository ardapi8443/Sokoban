package sokoban.model;

import javafx.collections.ObservableSet;
import javafx.stage.FileChooser;

import java.io.*;
import java.util.stream.Stream;

class FileHandler {

    private final FileChooser fileChooser = new FileChooser();

    Board4Design board4Design;

   public FileHandler(Board4Design board4Design){
        this.board4Design = board4Design;
    }


    public void save() {
        String saveContent =board4Design.phrase();

        File selectedFile = fileChooser.showSaveDialog(null);
        File file;
        if (selectedFile != null){
            file = new File(selectedFile.toURI());
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))){
                writer.write(saveContent);
                file.createNewFile();
            } catch (Exception e ){
                System.out.println(e.getMessage());
            }
        }
    }
    public String open() {
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            StringBuilder fileContent = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new FileReader(selectedFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    fileContent.append(line).append("\n");
                }
                return fileContent.toString();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        return null;
    }

}
