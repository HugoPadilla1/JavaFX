package HWs.HW2;

/**
 * Class: VideoGameApp
 * @author Hugo
 * @version 1.0
 * Course: ITEC 3150 Fall 2024
 * Written: October 6th, 2024
 *
 * This class – This is the VideoGameApp Class, which utilizes JavaFX to create a graphical user interface (GUI) that allows users to enter,
 * display, rate, and save video game information. The GUI includes text fields for entering a video game's name, platform, and rating.
 * Users can add video games, print the list of games, rate the games, and save the data to a binary file. The application handles input validation,
 * ensuring that valid data is entered for each video game. The interface also provides error handling, displaying alert boxes for invalid inputs
 * or other exceptions. The clear and exit buttons provide functionality to reset fields and close the application, respectively.
 */

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.*;
import java.util.*;

public class VideoGameApp extends Application {
    private Set<VideoGame> games = new TreeSet<>();
    private Map<VideoGame, ArrayList<Integer>> gameRatingsMap = new HashMap<>();

    @Override
    public void start(Stage primaryStage) {
        // Text fields for Name, Platform, and Rating
        TextField nameField = new TextField();
        nameField.setPromptText("Enter game name");

        TextField platformField = new TextField();
        platformField.setPromptText("Enter platform (Xbox, PS5, PC)");

        TextField ratingField = new TextField();
        ratingField.setPromptText("Enter rating (E, T, M)");

        // Buttons for Enter, Print, Ratings, Save, and Exit
        Button enterButton = new Button("Enter");
        Button printButton = new Button("Print");
        Button ratingsButton = new Button("Ratings");
        Button saveButton = new Button("Save");
        Button exitButton = new Button("Exit");

        // Event handling for Enter button
        enterButton.setOnAction(e -> {
            try {
                String name = nameField.getText();
                String platform = platformField.getText();
                String rating = ratingField.getText();
                VideoGame game = new VideoGame(name, platform, rating);

                if (!games.add(game)) {
                    showAlert("Duplicate Entry", "This game already exists in the list.");
                } else {
                    showAlert("Success", "Game added: " + game.toString());
                    // Clear input fields after entering a game
                    nameField.clear();
                    platformField.clear();
                    ratingField.clear();
                }

                // Debug: Print the game added to console to ensure it's working
                System.out.println("Game added: " + game.toString());

            } catch (IllegalArgumentException ex) {
                showAlert("Error", ex.getMessage());
            }
        });

        // Event handling for Print button
        printButton.setOnAction(e -> printGames());

        // Event handling for Ratings button
        ratingsButton.setOnAction(e -> {
            if (games.isEmpty()) {
                showAlert("Error", "No games to rate.");
                return;
            }
            rateGames();
        });

        // Event handling for Save button
        saveButton.setOnAction(e -> writeToFile());

        // Event handling for Exit button
        exitButton.setOnAction(e -> Platform.exit());

        // Layout for buttons
        HBox buttonLayout = new HBox(10, enterButton, printButton, ratingsButton, saveButton, exitButton);

        // Main layout
        VBox layout = new VBox(10, nameField, platformField, ratingField, buttonLayout);
        layout.setSpacing(10);

        // Set up the scene and the stage
        Scene scene = new Scene(layout, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Video Game Rating App");
        primaryStage.show();
    }

    // Method to print games
    private void printGames() {
        if (games.isEmpty()) {
            showAlert("Error", "No games to display.");
        } else {
            StringBuilder gamesList = new StringBuilder("Games List:\n");
            for (VideoGame game : games) {
                gamesList.append(game).append("\n");
            }
            // Show all entered games in an alert
            showAlert("Video Games", gamesList.toString());

            // Debug: Print games to console to ensure they are added to the set
            System.out.println(gamesList);
        }
    }

    // Method to rate games
    private void rateGames() {
        for (VideoGame game : games) {
            ArrayList<Integer> ratings = new ArrayList<>();
            int rating;
            do {
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Rate Game");
                dialog.setHeaderText("Enter rating for " + game.getName() + " (1-10), or 0 to stop:");
                dialog.setContentText("Rating:");
                Optional<String> result = dialog.showAndWait();
                if (result.isPresent()) {
                    try {
                        rating = Integer.parseInt(result.get());
                        if (rating == 0 && ratings.isEmpty()) {
                            showAlert("Error", "You must enter at least one rating.");
                        } else if (rating >= 1 && rating <= 10) {
                            ratings.add(rating);
                        }
                    } catch (NumberFormatException e) {
                        showAlert("Invalid Input", "Please enter a valid number.");
                        rating = -1; // keep the loop running in case of invalid input
                    }
                } else {
                    rating = 0;
                }
            } while (rating != 0);
            gameRatingsMap.put(game, ratings);
        }
        printGameRatings();
    }

    // Method to print the games and their ratings
    private void printGameRatings() {
        for (Map.Entry<VideoGame, ArrayList<Integer>> entry : gameRatingsMap.entrySet()) {
            VideoGame game = entry.getKey();
            ArrayList<Integer> ratings = entry.getValue();
            double average = ratings.stream().mapToInt(Integer::intValue).average().orElse(0);
            System.out.println(game.getName() + ": " + ratings.size() + " ratings, average: " + average);
        }
    }

    // Method to save the games to a file
    private void writeToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("games.dat"))) {
            oos.writeObject(gameRatingsMap);
            showAlert("Success", "Games saved to file.");
        } catch (IOException e) {
            showAlert("Error", "Failed to write to file.");
        }
    }

    // Method to display alerts
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

