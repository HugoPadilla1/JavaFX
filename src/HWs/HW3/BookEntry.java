package HWs.HW3;

/**
 * Class: BookEntry
 * Author: Hugo Padilla
 * Version: 1.0
 * Course: ITEC 3150 Fall 2024
 * Written: November 16th, 2024
 *
 * This class – The `BookEntry` class provides the JavaFX graphical user interface (GUI) for managing book data.
 * Users can input details about a book, including its title, author, genre, library status, and number of chapters.
 * The interface includes buttons for saving, displaying, and clearing book data, as well as exiting the application.
 * The GUI enforces input validation and provides alerts for errors.
 *
 */

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class BookEntry extends Application {

    private BookEntryController controller = new BookEntryController();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Book Entry");

        // Form controls
        TextField titleField = new TextField();
        TextField authorField = new TextField();
        // Drop Down for Genres
        ComboBox<String> genreComboBox = new ComboBox<>();
        genreComboBox.getItems().addAll("History", "Military", "Science Fiction", "Fantasy", "Romance");
        genreComboBox.setValue("History"); // Set default value
        CheckBox inLibraryCheckBox = new CheckBox("In Library");
        TextField libraryNameField = new TextField();
        libraryNameField.setDisable(true); // Disabled by default
        TextField numChaptersField = new TextField();

        // Enable libraryNameField only if 'inLibrary' is checked
        inLibraryCheckBox.setOnAction(event -> {
            libraryNameField.setDisable(!inLibraryCheckBox.isSelected());
        });

        // Buttons
        Button saveButton = new Button("Save");
        Button displayButton = new Button("Display Books");
        Button closeButton = new Button("Close");

        // Button actions
        saveButton.setOnAction(event -> {
            try {
                String title = titleField.getText();
                String author = authorField.getText();
                String genre = genreComboBox.getValue();
                boolean inLibrary = inLibraryCheckBox.isSelected();
                String libraryName = libraryNameField.getText();
                int numChapters = Integer.parseInt(numChaptersField.getText());

                Book book = new Book(title, author, genre, inLibrary, libraryName, numChapters);
                controller.saveData(book);

                // Reset form fields
                titleField.clear();
                authorField.clear();
                genreComboBox.setValue("History");
                inLibraryCheckBox.setSelected(false);
                libraryNameField.clear();
                libraryNameField.setDisable(true);
                numChaptersField.clear();

                showAlert(Alert.AlertType.INFORMATION, "Success", "Book saved successfully!");
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Please enter a valid number for the chapters.");
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", "An error occurred: " + e.getMessage());
            }
        });

        displayButton.setOnAction(event -> controller.display(primaryStage));

        closeButton.setOnAction(event -> controller.close(primaryStage));

        // Layout
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        gridPane.add(new Label("Title:"), 0, 0);
        gridPane.add(titleField, 1, 0);
        gridPane.add(new Label("Author:"), 0, 1);
        gridPane.add(authorField, 1, 1);
        gridPane.add(new Label("Genre:"), 0, 2);
        gridPane.add(genreComboBox, 1, 2);
        gridPane.add(inLibraryCheckBox, 1, 3);
        gridPane.add(new Label("Library Name:"), 0, 4);
        gridPane.add(libraryNameField, 1, 4);
        gridPane.add(new Label("Number of Chapters:"), 0, 5);
        gridPane.add(numChaptersField, 1, 5);

        VBox vBox = new VBox(10, gridPane, saveButton, displayButton, closeButton);
        vBox.setPadding(new Insets(10));

        Scene scene = new Scene(vBox, 600, 400);
        scene.getStylesheets().add(getClass().getResource("librarystyle.css").toExternalForm());

//        primaryStage.setScene(new Scene(vBox, 400, 300));
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
