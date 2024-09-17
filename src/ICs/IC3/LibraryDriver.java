package ICs.IC3;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class LibraryDriver extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        GridPane grid = new GridPane();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("library.fxml"));
        grid.add(loader.load(), 0, 0);
        Scene scene = new Scene(grid);
        stage.setScene(scene);
        stage.show();
    }
}
