/**
 * Class: CourseManagerMain
 * @author Hugo Padilla
 * @version 1.0
 * Course: ITEC 3150 Fall 2024
 * Written: September 25th, 2024
 *
 * This class – This is our CourseManagerMain, which utilizes JavaFX in order to present a GUI to the end-user.
 * This GUI contains text fields for entering course names and student names, and buttons for adding courses,
 * adding students, and exiting the application. The GUI is separated into two stages:
 * - In the first stage, users can add courses. After finishing, they are prompted to continue to the
 *   student entry phase.
 * - In the second stage, users can add students to each course. The current course name is displayed
 *   on the screen, and users can transition between courses automatically once students are added.
 * The user is also prevented from finalizing a course unless at least one student is added. Buttons are
 * arranged horizontally, and there are error prompts for invalid inputs or actions, such as trying to
 * proceed without adding students to a course.
 */
package ICs.IC4;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.Map;
import java.util.Iterator;


public class CourseManagerMain extends Application {
    private final CourseManager courseManager = new CourseManager();
    private TextField courseField;
    private TextField studentField;
    private Button addCourseButton, addStudentButton;
    private Label currentCourseLabel;  // Label to show the current course in the student screen
    private String currentCourse;  // Track current course being added
    private Iterator<String> courseIterator;
    private Scene courseScene, studentScene;
    private Stage window;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        window = primaryStage;
        window.setTitle("Course Information");

        // Set up the Course Entry Scene
        courseField = new TextField();
        courseField.setPromptText("Course Name");

        addCourseButton = new Button("Add Course");
        addCourseButton.setOnAction(e -> addCourse());

        Button proceedToAddStudentsButton = new Button("Add Students");
        proceedToAddStudentsButton.setOnAction(e -> confirmAddStudents());

        Button exitPrimaryButton = new Button("Exit");
        exitPrimaryButton.setOnAction(e -> {
            window.close();
            Platform.exit();  // Ensure smooth closure when exiting from the main screen
        });

        // Arrange all buttons (Add Course, Add Students, Exit) side by side
        HBox courseButtons = new HBox(10);
        courseButtons.getChildren().addAll(addCourseButton, proceedToAddStudentsButton, exitPrimaryButton);

        VBox courseLayout = new VBox(10);
        courseLayout.setPadding(new Insets(20, 20, 20, 20));
        courseLayout.getChildren().addAll(courseField, courseButtons);  // Add the HBox containing all buttons

        courseScene = new Scene(courseLayout, 400, 200);  // Adjust the width for horizontal buttons

        // Set up the Student Entry Scene
        studentField = new TextField();
        studentField.setPromptText("Student Name");

        currentCourseLabel = new Label();  // This will show the current course being worked on

        addStudentButton = new Button("Add Student");
        addStudentButton.setOnAction(e -> addStudents());

        Button exitButton = new Button("Exit");
        exitButton.setOnAction(e -> {
            displayCourseRoles();  // Display courses and students before exiting
            window.close();
        });

        // Arrange buttons side by side for student screen
        HBox studentButtons = new HBox(10);
        studentButtons.getChildren().addAll(addStudentButton, exitButton);

        VBox studentLayout = new VBox(10);
        studentLayout.setPadding(new Insets(20, 20, 20, 20));
        studentLayout.getChildren().addAll(currentCourseLabel, studentField, studentButtons);

        studentScene = new Scene(studentLayout, 400, 200);

        // Start with the course entry scene
        window.setScene(courseScene);
        window.show();
    }

    // Method to handle course addition
    private void addCourse() {
        String courseName = courseField.getText().trim();

        if (courseName.isEmpty()) {
            showError("Error", "Course name cannot be blank.");
        } else {
            boolean success = courseManager.addCourse(courseName);
            if (!success) {
                showError("Error", "Course name already exists.");
            } else {
                showConfirmation("Success", "Course added successfully.");
                courseField.clear();
            }
        }
    }

    // Confirmation to proceed to student adding phase or go back to add more courses
    private void confirmAddStudents() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Proceed to Add Students?");
        alert.setContentText("Do you want to continue adding students or go back to add more courses?");

        ButtonType continueButton = new ButtonType("Continue");
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(continueButton, cancelButton);

        alert.showAndWait().ifPresent(response -> {
            if (response == continueButton) {
                if (courseManager.getCourseCount() > 0) {
                    startAddingStudents();
                    window.setScene(studentScene);  // Switch to student adding scene
                } else {
                    showError("Error", "Please add at least one course before proceeding.");
                }
            }
        });
    }

    // Initialize the student-adding phase
    private void startAddingStudents() {
        courseIterator = courseManager.getCourseIterator();  // Get the iterator
        if (courseIterator.hasNext()) {
            currentCourse = courseIterator.next();  // Start with the first course
            currentCourseLabel.setText("Adding students to course: " + currentCourse);  // Show current course name
            showConfirmation("Add Students", "Now adding students to " + currentCourse);
        }
    }

    // Method to handle student addition
    private void addStudents() {
        String studentName = studentField.getText().trim();

        if (studentName.isEmpty()) {
            // Check if the current course has at least one student
            if (courseManager.getStudentsCount(currentCourse) == 0) {
                showError("Error", "The course '" + currentCourse + "' must have at least one student.");
            } else {
                // Move to the next course when a blank student name is entered
                if (courseIterator.hasNext()) {
                    currentCourse = courseIterator.next();
                    currentCourseLabel.setText("Adding students to course: " + currentCourse);
                    showConfirmation("Next Course", "Now adding students to " + currentCourse);
                } else {
                    showConfirmation("Finished", "All courses have students.");
                    displayCourseRoles();  // Display all courses and students
                    window.setScene(courseScene);  // Return to the main course screen
                }
            }
        } else {
            courseManager.addStudentToCourse(currentCourse, studentName);
            showConfirmation("Success", "Student added to course " + currentCourse);
            studentField.clear();
        }
    }

    // Method to display all courses and their student roles at the end
    private void displayCourseRoles() {
        StringBuilder message = new StringBuilder("Course Roles:\n");
        for (Map.Entry<String, ArrayList<String>> entry : courseManager.getCourseMap().entrySet()) {
            message.append(entry.getKey()).append(":\n");
            for (String student : entry.getValue()) {
                message.append(" - ").append(student).append("\n");
            }
        }
        showInfo("Course Roles", message.toString());
    }

    // Utility method for showing error dialogs
    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Utility method for showing confirmation dialogs
    private void showConfirmation(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Utility method for showing information dialogs
    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}