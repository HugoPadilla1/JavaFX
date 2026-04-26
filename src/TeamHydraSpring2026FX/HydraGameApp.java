package TeamHydraSpring2026FX;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * JavaFX launcher for the Team Hydra game.
 * Keep this class small; all gameplay/UI behavior lives in HydraGameController.
 */
public class HydraGameApp extends Application {

    @Override
    public void start(Stage stage) {
        HydraGameController controller = new HydraGameController();
        Scene scene = new Scene(controller.createView(), 1180, 760);
        stage.setTitle("Team Hydra - Hospital Escape");
        stage.setScene(scene);
        stage.setMinWidth(1000);
        stage.setMinHeight(680);
        stage.show();
        controller.startNewGame();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
