package TextBasedGame;
import java.io.*;
import java.util.*;

public class AnimalAdventureGame {
    static Map<Integer, Room> rooms = new HashMap<>();
    static int currentRoom = 1; // starting room

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter Rooms.txt path (or press Enter for 'Rooms.txt'): ");
        String path = scanner.nextLine().trim();
        if (path.isEmpty()) path = "Rooms.txt";
        loadRooms(path);

        System.out.println("Welcome to the Animal Adventure Game!");
        System.out.println("Explore the zoo by typing directions: NORTH, SOUTH, EAST, WEST, or QUIT to exit.\n");

        boolean playing = true;
        while (playing) {
            Room room = rooms.get(currentRoom);
            System.out.println(room.getInfo());
            room.visit();

            System.out.print("Enter direction (NORTH/SOUTH/EAST/WEST/QUIT): ");
            String input = scanner.nextLine().toUpperCase();

            if (input.equals("QUIT")) {
                playing = false;
                System.out.println("Thanks for playing!");
            } else if (room.exits.containsKey(input)) {
                currentRoom = room.exits.get(input);
            } else {
                System.out.println("You can't go this way.");
            }
        }
        scanner.close();
    }

    // Load room data from Rooms.txt.txt
    public static void loadRooms(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Format: roomNumber,roomName,roomDescription,Exit1:Room#,Exit2:Room#
                String[] parts = line.split(",", 4);
                int roomNumber = Integer.parseInt(parts[0].trim());
                String roomName = parts[1].trim();
                String roomDesc = parts[2].trim();

                Room room = new Room(roomNumber, roomName, roomDesc);

                // Parse exits
                if (parts.length > 3) {
                    String[] exits = parts[3].split(",");
                    for (String exit : exits) {
                        String[] dir = exit.split(":");
                        if (dir.length == 2) {
                            room.addExit(dir[0].trim().toUpperCase(), Integer.parseInt(dir[1].trim()));
                        }
                    }
                }
                rooms.put(roomNumber, room);
            }
        } catch (IOException e) {
            System.out.println("Error loading rooms: " + e.getMessage());
        }
    }
}
