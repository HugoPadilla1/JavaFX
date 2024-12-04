import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;

public class FileChooserTest extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("FileChooser Test");
        // Implementing the button to call FileChooser
        Button openButton = new Button("Open FileChooser");
        openButton.setOnAction(e -> {
            File selectedFile = showFileChooser(primaryStage);  // Call the FileChooser method
            if (selectedFile != null) {
                System.out.println("Selected file: " + selectedFile.getAbsolutePath());
            } else {
                System.out.println("No file selected");
            }
        });

        // Setting the scene, as well as, VBOX.
        VBox layout = new VBox(20);
        layout.getChildren().add(openButton);

        Scene scene = new Scene(layout, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Method to open FileChooser and return the selected file
    public File showFileChooser(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open File");

        // Show the file chooser dialog
        return fileChooser.showOpenDialog(stage);  // Return the selected file
    }
}