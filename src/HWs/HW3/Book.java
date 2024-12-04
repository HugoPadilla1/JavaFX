package HWs.HW3;

/**
 * Class: Book
 * Author: Hugo Padilla
 * Version: 1.0
 * Course: ITEC 3150 Fall 2024
 * Written/Altered: November 15th, 2024
 *
 * This class – The `Book` class represents a book object with fields for the book's title, author, genre, library status, library name,
 * and the number of chapters. It includes input validation to ensure that valid data is entered for each field.
 * Default values are applied when invalid data is detected, and alerts are displayed to notify the user.
 *
 */

import java.io.Serializable;
import javafx.scene.control.Alert;

public class Book implements Serializable {
    private static final long serialVersionUID = 1L;
    private String title;
    private String author;
    private String genre;
    private boolean inLibrary;
    private String libraryName;
    private int numChapters;

    public Book() {
        this.title = "This is a book";
        this.author = "author";
        this.genre = "History";
        this.inLibrary = false;
        this.libraryName = "";
        this.numChapters = 20;
    }

    public Book(String title, String author, String genre, boolean inLibrary, String libraryName, int numChapters){
        setTitle(title);
        setAuthor(author);
        setGenre(genre);
        setInLibrary(inLibrary);
        setLibraryName(libraryName);
        setNumChapters(numChapters);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) { // if-else for validating title
        if(title != null && title.length() >= 2){
            this.title = title;
        } else if (title == null || title.isEmpty() || title.length() < 2){
            showAlert(Alert.AlertType.ERROR, "Invalid Title", "Title field fails validation, ensure it contains > 2 characters, and is not empty. Resorting to default value.");
            this.title = "This is a book.";
        }
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) { // if-else for validating author
        if(author != null && author.length() >= 5){
            this.author = author;
        } else if (author == null || author.isEmpty() || author.length() < 5){
            showAlert(Alert.AlertType.ERROR, "Invalid Author", "Author field fails validation, ensure it contains > 5 characters, and is not blank. Resorting to default value.");
            this.author = "author";
        }
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) { //if-else for validating Genre is a pre-defined genre
        if (genre != null && (genre.equals("History") || genre.equals("Military") || genre.equals("Science Fiction")
                || genre.equals("Fantasy") || genre.equals("Romance"))) {
            this.genre = genre;
        } else {
            this.genre = "History";
        }
    }

    public boolean isInLibrary() {
        return inLibrary;
    }

    public void setInLibrary(boolean inLibrary) {
        this.inLibrary = inLibrary;
    }

    public String getLibraryName() {
        return libraryName;
    }

    public void setLibraryName(String libraryName) {
        if (inLibrary && (libraryName != null && libraryName.split(" ").length >= 2)) {
            this.libraryName = libraryName;
        } else {
            this.libraryName = "";
        }
    }

    public int getNumChapters() {
        return numChapters;
    }

    public void setNumChapters(int numChapters) {
        if (numChapters >= 1 && numChapters <= 100) {
            this.numChapters = numChapters;
        } else {
            this.numChapters = 10;
        }
    }

    @Override
    public String toString() {
        return String.format(
                "Book title: %s\nauthor: %s\ngenre: %s\nnumber of chapters: %d\nin library: %s%s\n",
                title, author, genre, numChapters, inLibrary ? "yes" : "no", inLibrary ? " library name: " + libraryName : "");
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null); // No header text
        alert.setContentText(message);
        alert.showAndWait(); // Waits for the user to close the alert before continuing
    }
}
