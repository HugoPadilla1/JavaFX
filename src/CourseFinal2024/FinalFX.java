package CourseFinal2024;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.geometry.Insets;


public class FinalFX extends Application {
    private TextField nameField;
    private TextField majorField;
    private Label resultLabel;

    @Override
    public void start(Stage primaryStage) {
        // Title Label
        Label titleLabel = new Label("3150 Final Fall 2024");
        titleLabel.setFont(Font.font("Arial", 26));
        titleLabel.setTextFill(Color.PURPLE);

        // Name Input
        Label nameLabel = new Label("What is your name?");
        nameField = new TextField();

        // Major Input
        Label majorLabel = new Label("What is your Major?");
        majorField = new TextField();

        // Buttons
        Button printButton = new Button("Print");
        Button clearButton = new Button("Clear");
        Button exitButton = new Button("Exit");

        // Event Handlers
        printButton.setOnAction(e -> handlePrint());
        clearButton.setOnAction(e -> handleClear());
        exitButton.setOnAction(e -> Platform.exit());

        // Layout for buttons
        HBox buttonLayout = new HBox(10, printButton, clearButton, exitButton);

        // Result Label
        resultLabel = new Label();
        resultLabel.setFont(Font.font("Arial", 16));
        resultLabel.setTextFill(Color.BLUE);

        // Main Layout
        VBox mainLayout = new VBox(15, titleLabel, nameLabel, nameField, majorLabel, majorField, buttonLayout, resultLabel);
        mainLayout.setPadding(new Insets(40));

        // Scene and Stage Setup
        Scene scene = new Scene(mainLayout, 500, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void handlePrint() {
        String name = nameField.getText().trim();
        String major = majorField.getText().trim();

        if (name.isEmpty()) {
            showAlert("Error", "Name cannot be blank");
            return;
        }

        if (!major.equalsIgnoreCase("IT") && !major.equalsIgnoreCase("Information Technology")) {
            showAlert("Error", "Major must be either IT or Information Technology");
            return;
        }

        resultLabel.setText(String.format("%s majoring in %s", name, major));
    }

    private void handleClear() {
        nameField.clear();
        majorField.clear();
        resultLabel.setText("");
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
