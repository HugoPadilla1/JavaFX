/**Class: TeamGenerator
 * @author Hugo
 * @version 1.0
 * Course: ITEC 3150 Fall 2024
 * Written: September 15th, 2024
 *
 * This class – This is our main class, which initializes the GUI. This loads from our teamEntry.fxml,
 * which defines the setup for our GUI.
 */
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
public class TeamGenerator extends Application{
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("teamEntry.fxml")); // our root fxml
        Scene scene = new Scene(root);
        primaryStage.setTitle("Team Sheet");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args){
        launch(args);
    }
}
