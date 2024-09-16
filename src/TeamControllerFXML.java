import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;

import java.awt.*;

public class TeamControllerFXML {
    @FXML
    private TextField nameTF;

    @FXML
    private TextField winsTF;

    @FXML
    private TextField lossesTF;

    @FXML
    private TextField mascotTF;

    @FXML
    private TextField returningTF;

    @FXML
    public void generate() {
        try {
            String teamName = nameTF.getText();
            int teamWins = Integer.parseInt(winsTF.getText());
            int teamLosses = Integer.parseInt(lossesTF.getText());
            String mascot = mascotTF.getText();
            int returningPlayers = Integer.parseInt(returningTF.getText());

            Team team = new Team(teamName, teamWins, teamLosses, mascot, returningPlayers);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Team was generated.");
        } catch (NumberFormatException e){
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Wins, Losses, and returning Players are integers.");
        } catch (TeamValidationException e){
            showAlert(Alert.AlertType.ERROR, "Validation Error!", e.getMessage());
        }
    }

    @FXML
    public void cancel() {
        nameTF.clear();
        winsTF.clear();
        lossesTF.clear();
        mascotTF.clear();
        returningTF.clear();

        Stage stage = (Stage) nameTF.getScene().getWindow();
        stage.close();
    }

    private void showAlert(Alert.AlertType type, String title, String message){
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
