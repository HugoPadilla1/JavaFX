package HWs.HW3;

/**
 * Class: BookFile
 * Author: Hugo Padilla
 * Version: 1.0
 * Course: ITEC 3150 Fall 2024
 * Written: November 15th, 2024
 *
 * This class – The `BookFile` class manages file operations for saving and retrieving `Book` objects to and from a binary file.
 * It ensures that all data is stored persistently and can be retrieved for display in the application.
 *
 * Methods:
 * - `writeFile(Book book)`: Saves a `Book` object to a binary file. Uses `AppendingObjectOutputStream` to append objects without
 *   overwriting the file.
 * - `readFile()`: Reads all `Book` objects from the binary file and returns them as an `ArrayList<Book>`.
 *
 */

import java.io.*;
import java.util.ArrayList;

public class BookFile {
    private static final String FILE_NAME = "books.dat";

    public static void writeFile(Book book) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(FILE_NAME, true);
             ObjectOutputStream oos = fos.getChannel().position() == 0
                     ? new ObjectOutputStream(fos)
                     : new AppendingObjectOutputStream(fos)) {
            oos.writeObject(book);
        }
    }

    public static ArrayList<Book> readFile() throws IOException, ClassNotFoundException {
        ArrayList<Book> books = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            while (true) {
                try {
                    books.add((Book) ois.readObject());
                } catch (EOFException e) {
                    break;
                }
            }
        }
        return books;
    }

    // Helper class for appending to the ObjectOutputStream
    private static class AppendingObjectOutputStream extends ObjectOutputStream {
        public AppendingObjectOutputStream(OutputStream out) throws IOException {
            super(out);
        }

        @Override
        protected void writeStreamHeader() throws IOException {
            reset();
        }
    }
}
