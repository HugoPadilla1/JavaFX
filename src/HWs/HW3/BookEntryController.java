package HWs.HW3;

/**
 * Class: BookEntryController
 * Author: Hugo Padilla
 * Version: 1.0
 * Course: ITEC 3150 Fall 2024
 * Written: November 15th, 2024
 *
 * This class – The `BookEntryController` class acts as the controller in the Model-View-Controller (MVC) architecture for the book application.
 * It handles user interactions, validates data, and coordinates saving and displaying book information.
 *
 */

import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.layout.*;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.io.*;
public class BookEntryController {
    public void saveData(Book book) {
        try {
            BookFile.writeFile(book);
        } catch (IOException e) {
            System.err.println("Error saving data: " + e.getMessage());
        }
    }

    public void display(Stage stage) {
        try {
            ArrayList<Book> books = BookFile.readFile();

            Stage displayStage = new Stage();
            displayStage.setTitle("Books");

            VBox vBox = new VBox(10);
            vBox.setPadding(new Insets(10));

            if (books.isEmpty()) {
                vBox.getChildren().add(new Label("No books available."));
            } else {
                for (Book book : books) {
                    Label bookLabel = new Label(book.toString());
                    bookLabel.setStyle("-fx-border-color: black; -fx-padding: 5;");
                    vBox.getChildren().add(bookLabel);
                }
            }

            ScrollPane scrollPane = new ScrollPane(vBox);
            scrollPane.setFitToWidth(true);

            Scene scene = new Scene(scrollPane, 400, 300);
            displayStage.setScene(scene);
            displayStage.show();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error reading books: " + e.getMessage());
            showAlert(Alert.AlertType.ERROR, "Error", "Unable to display books.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void close(Stage stage) {
        stage.close();
    }
}
