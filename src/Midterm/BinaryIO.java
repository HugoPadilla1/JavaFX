package Midterm;

import java.io.*;
import java.util.Arrays;
public class BinaryIO {
    public static void main(String[] args) {
        // create instance
        VideoGame[] games = new VideoGame[5];
        games[0] = new VideoGame("Borderlands", "First Person Shooter", 'M', 18);
        games[1] = new VideoGame("Portal", "Puzzle", 'T', 15);
        games[2] = new VideoGame("Lego Indiana Jones", "Puzzle", 'E', 6);
        games[3] = new VideoGame("Diablo 4", "RPG", 'T', 15);
        games[4] = new VideoGame("World of Warcraft", "RPG", 'M', 18);


        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("gameData.dat"))) {
            oos.writeObject(games);
            System.out.println("Array of VideoGame objects has been written to gameData.dat");
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Write code to create a binary file in the default directory
        // named "gameData.dat" using ObjectOutputStream

        VideoGame[] readGames = null;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("gameData.dat"))) {
            readGames = (VideoGame[]) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        //Read the file
        //Sort the array you just read
        if (readGames != null) {
            Arrays.sort(readGames);

            // Print the contents of the array using the Midterm.VideoGame toString method
            for (VideoGame game : readGames) {
                System.out.println(game);
            }

        }
    }
}
