/**Class: TeamControllerFXML
 * @author Hugo
 * @version 1.0
 * Course: ITEC 3150 Fall 2024
 * Written: September 14th, 2024
 *
 * This class – This is our TeamControllerFXML Class, which utilizes JavaFX and FXML in order to present a GUI to the end-user.
 * This GUI contains Text Fields that are used for filling in all attributes for the required Team object.
 * These TextFields contain the attributes that initialize the Team object. The team object is then created, and displayed
 * to the end-user. If there are errors, a separate error alert box appears detailing the exception. There is a clear and ok button,
 * the okay button attempts to create the object, while the cancel clears the fields and closes the scene.
 */

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
        try { // this attempts to grab the text and integers fromo the fields and apply them to our object.
            String teamName = nameTF.getText();
            int teamWins = Integer.parseInt(winsTF.getText());
            int teamLosses = Integer.parseInt(lossesTF.getText());
            String mascot = mascotTF.getText();
            int returningPlayers = Integer.parseInt(returningTF.getText());

            Team team = new Team(teamName, teamWins, teamLosses, mascot, returningPlayers);

            // the alerts are displayed when an exception occurs or successfully initialized
            showAlert(Alert.AlertType.INFORMATION, "Success", "Team was generated. " + team.toString());
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

        Stage stage = (Stage) nameTF.getScene().getWindow(); // i use this to find the stage we are in
        stage.close();
    }

    private void showAlert(Alert.AlertType type, String title, String message){
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
