package Midterm;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.layout.HBox;

public class JavaFXDemo extends Application {

    private TextArea textArea;

    @Override
    public void start(Stage primaryStage) {
        // Create Buttons && Label
        Button helloButton = new Button("Hello");
        Button byeButton = new Button("Bye");
        Button closeButton = new Button("Close");
        Label promptLabel = new Label("Click a button");

        // Create TextArea
        textArea = new TextArea();
        textArea.setEditable(false);

        // Button Event Handlers
        helloButton.setOnAction(e -> handleHelloButtonClick());
        byeButton.setOnAction(e -> handleByeButtonClick());
        closeButton.setOnAction(e -> {
            System.out.println("Calling Platform.exit().");
            Platform.exit();
        }); // Close the application using Platform.exit()

        // Layout for buttons
        VBox buttonBox = new VBox(20);
        buttonBox.getChildren().addAll(helloButton, byeButton, closeButton, promptLabel);

        // Layout for entire window
        HBox layout = new HBox(15);
        layout.getChildren().addAll(buttonBox, textArea);

        // Create Scene and Stage
        Scene scene = new Scene(layout, 400, 200);
        primaryStage.setTitle("FX Demo");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Handle Hello Button Click
    private void handleHelloButtonClick() {
        textArea.setText("You have pressed the\nHello button.\nWhat would you like to do?");
    }

    // Handle Bye Button Click
    private void handleByeButtonClick() {
        textArea.appendText("\nLeaving so soon?\nCome back soon.");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
