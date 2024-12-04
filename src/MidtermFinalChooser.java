import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
public class MidtermFinalChooser {
    public File showFileChooser(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("File Opener");

//        // Optional: Set initial directory
//        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
//
//        // Optional: Add file extension filters
//        fileChooser.getExtensionFilters().addAll(
//                new FileChooser.ExtensionFilter("Text Files", "*.txt"),
//                new FileChooser.ExtensionFilter("All Files", "*.*")
//        );

        // Show the dialog and get the selected file
        File selectedFile = fileChooser.showOpenDialog(stage);
        return selectedFile;  // Return the selected file object
    }
}
