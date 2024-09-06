/**Class: TeamEntry
 * @author Hugo
 * @version 1.0
 * Course: ITEC 3150 Fall 2024
 * Written: September 5th, 2024
 *
 * This class – This is our TeamEntry, which utilizes JavaFX in order to present a GUI to the end-user.
 * This GUI contains boxes that are used for filling in all attributes for the required Team object.
 * Each box is spaced and their are two buttons used for confirming or clearing the fields.
 * There are three cases of error, being that our integers are not integers, and each exception for each attribute.
 * In addition to this, there is an unexpected error in case I missed anything.
 * These exceptions are caught and displayed to the end user.
 */
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
public class TeamEntry extends Application{
    private TextField teamNameField;
    private TextField winsField;
    private TextField lossesField;
    private TextField mascotField;
    private TextField returningPlayersField;
    private Label messageLabel;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Team Entry Dialog");

        // Prompt Label - For entering information.
        Label promptLabel = new Label("Enter Team Information:");

        // Team Name
        Label teamNameLabel = new Label("Team Name:");
        teamNameField = new TextField();
        HBox teamNameBox = new HBox(10, teamNameLabel, teamNameField);

        // Last Year's Wins
        Label winsLabel = new Label("Last Year's Wins:");
        winsField = new TextField();
        HBox winsBox = new HBox(10, winsLabel, winsField);

        // Last Year's Losses
        Label lossesLabel = new Label("Last Year's Losses:");
        lossesField = new TextField();
        HBox lossesBox = new HBox(10, lossesLabel, lossesField);

        // Mascot
        Label mascotLabel = new Label("Mascot:");
        mascotField = new TextField();
        HBox mascotBox = new HBox(10, mascotLabel, mascotField);

        // Returning Players
        Label returningPlayersLabel = new Label("Returning Players:");
        returningPlayersField = new TextField();
        HBox returningPlayersBox = new HBox(10, returningPlayersLabel, returningPlayersField);

        // Message Label
        messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: red;");

        // Buttons
        Button okButton = new Button("OK");
        okButton.setOnAction(new OkButtonHandler());

        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> clearFields());

        HBox buttonBox = new HBox(10, okButton, cancelButton);

        // Main Layout
        VBox mainLayout = new VBox(15);
        mainLayout.setPadding(new Insets(20));
        mainLayout.getChildren().addAll(
                promptLabel,
                teamNameBox,
                winsBox,
                lossesBox,
                mascotBox,
                returningPlayersBox,
                messageLabel,
                buttonBox
        );

        Scene scene = new Scene(mainLayout, 400, 350);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private class OkButtonHandler implements javafx.event.EventHandler<javafx.event.ActionEvent> {
        @Override
        public void handle(javafx.event.ActionEvent event) {
            try {
                String teamName = teamNameField.getText();
                int wins = Integer.parseInt(winsField.getText());
                int losses = Integer.parseInt(lossesField.getText());
                String mascot = mascotField.getText();
                int returningPlayers = Integer.parseInt(returningPlayersField.getText());

                Team team = new Team(teamName, wins, losses, mascot, returningPlayers);

                showTeamDetails(team);

                clearFields();
                messageLabel.setText("");

            } catch (NumberFormatException e) {
                messageLabel.setText("Wins, Losses, and Returning Players must be integers.");
            } catch (TeamValidationException e) {
                messageLabel.setText(e.getMessage());
            } catch (Exception e) {
                messageLabel.setText("An unexpected error occurred.");
            }
        }
    }

    // Clears the fields if the button is hit.
    private void clearFields() {
        teamNameField.clear();
        winsField.clear();
        lossesField.clear();
        mascotField.clear();
        returningPlayersField.clear();
        messageLabel.setText("");
    }

    // Presents the details too the end user after button press.

    private void showTeamDetails(Team team) {
        Stage detailStage = new Stage();
        detailStage.setTitle("Team Details");

        TextArea teamDetailsArea = new TextArea(team.toString());
        teamDetailsArea.setEditable(false);

        VBox layout = new VBox(10, teamDetailsArea);
        layout.setPadding(new Insets(20));

        Scene scene = new Scene(layout, 300, 200);
        detailStage.setScene(scene);
        detailStage.show();
    }
}
