package Login;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class LoginFXML extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
        GridPane root = (GridPane) loader.load();
        Scene scene = new Scene(root, 300, 275);
        scene.getStylesheets().add(getClass().getResource("Login.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Login");
        stage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
