package textgame_update;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class AnimalAdventureGame {
    private static Map<Integer, Room> rooms = new HashMap<>();
    private static Map<String, Item> items = new HashMap<>();
    private static Map<Integer, Puzzle> puzzles = new HashMap<>();
    private static Player player;

    public static void main(String[] args) {
        //System.out.println(System.getProperty("user.dir"));
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter Rooms.txt path (or press Enter for 'Rooms.txt'): ");
        String roomsPath = scanner.nextLine().trim();
        if (roomsPath.isEmpty()) {
            roomsPath = "src/textgame_update/Rooms.txt";
        }

        System.out.print("Enter Items.txt path (or press Enter for 'Items.txt'): ");
        String itemsPath = scanner.nextLine().trim();
        if (itemsPath.isEmpty()) {
            itemsPath = "src/textgame_update/Items.txt";
        }

        System.out.print("Enter Puzzle.txt path (or press Enter for 'Puzzle.txt'): ");
        String puzzlePath = scanner.nextLine().trim();
        if (puzzlePath.isEmpty()) {
            puzzlePath = "src/textgame_update/Puzzle.txt";
        }

        loadRooms(roomsPath);
        loadItems(itemsPath);
        loadPuzzles(puzzlePath);
        player = new Player(1);

        System.out.println("Welcome to the Animal Adventure Game!");
        System.out.println("Use NORTH, SOUTH, EAST, WEST to move.");
        System.out.println("Use EXPLORE, PICKUP item-name, INSPECT item-name, DROP item-name, INVENTORY, or QUIT.\n");

        boolean playing = true;
        while (playing) {
            Room currentRoom = rooms.get(player.getCurrentRoom());
            System.out.println(currentRoom.getInfo());
            currentRoom.visit();
            handlePuzzle(currentRoom, scanner);

            System.out.print("Enter command: ");
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("QUIT")) {
                playing = false;
                System.out.println("Thanks for playing my game!");
            } else {
                processCommand(input, currentRoom, scanner);
            }
        }

        scanner.close();
    }

    public static void processCommand(String input, Room currentRoom, Scanner scanner) {
        if (input.equalsIgnoreCase("EXPLORE")) {
            exploreRoom(currentRoom);
            return;
        }

        if (input.equalsIgnoreCase("INVENTORY")) {
            showInventory();
            return;
        }

        if (input.toUpperCase().startsWith("PICKUP ")) {
            String itemName = input.substring(7).trim();
            pickUpItem(currentRoom, itemName);
            return;
        }

        if (input.toUpperCase().startsWith("INSPECT ")) {
            String itemName = input.substring(8).trim();
            inspectItem(itemName);
            return;
        }

        if (input.toUpperCase().startsWith("DROP ")) {
            String itemName = input.substring(5).trim();
            dropItem(currentRoom, itemName);
            return;
        }

        movePlayer(input, currentRoom);
    }

    public static void movePlayer(String direction, Room currentRoom) {
        if (currentRoom.hasExit(direction)) {
            player.setCurrentRoom(currentRoom.getExitRoom(direction));
        } else {
            System.out.println("You cannot go this way.");
        }
    }

    public static void exploreRoom(Room currentRoom) {
        List<Item> roomItems = currentRoom.getItemsInRoom(items);
        if (roomItems.isEmpty()) {
            System.out.println("There are no items to explore in this room.");
            return;
        }

        for (Item item : roomItems) {
            System.out.println(item.getName());
        }
    }

    public static void pickUpItem(Room currentRoom, String itemName) {
        Item item = currentRoom.findItemInRoom(itemName, items);
        if (item == null) {
            System.out.println("This item is not available in the current room.");
            return;
        }

        item.pickUp();
        player.addItem(item);
        System.out.println(item.getName() + " has been picked up from the room and successfully added to the player inventory.");
    }

    public static void inspectItem(String itemName) {
        Item item = player.getItemByName(itemName);
        if (item == null) {
            System.out.println("You must pick up this item before inspecting it.");
            return;
        }

        System.out.println(item.getDescription());
    }

    public static void dropItem(Room currentRoom, String itemName) {
        Item item = player.getItemByName(itemName);
        if (item == null) {
            System.out.println("You do not have this item in your inventory.");
            return;
        }

        player.removeItem(item);
        item.drop(currentRoom.getRoomNumber());
        System.out.println(item.getName() + " has been dropped successfully from the player inventory and placed in " + currentRoom.getName() + ".");
    }

    public static void showInventory() {
        if (player.getInventory().isEmpty()) {
            System.out.println("You have not picked up any items yet.");
            return;
        }

        for (Item item : player.getInventory()) {
            System.out.println(item.getName());
        }
    }

    public static void handlePuzzle(Room currentRoom, Scanner scanner) {
        if (!currentRoom.hasUnsolvedPuzzle(puzzles)) {
            return;
        }

        Puzzle puzzle = currentRoom.getPuzzle(puzzles);
        puzzle.resetAttempts();
        System.out.println(puzzle.getDescription());

        while (!puzzle.isSolved() && puzzle.hasAttemptsRemaining()) {
            System.out.print("Enter your answer: ");
            String answer = scanner.nextLine();

            if (puzzle.checkAnswer(answer)) {
                System.out.println("You solved the puzzle correctly!");
                return;
            }

            if (puzzle.hasAttemptsRemaining()) {
                System.out.println("The answer you provided is incorrect. You have "
                        + puzzle.getRemainingAttempts() + " attempt(s) remaining. Try again.");
            } else {
                System.out.println("You failed to solve the puzzle.");
            }
        }
    }

    public static void loadRooms(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", 4);
                int roomNumber = Integer.parseInt(parts[0].trim());
                String roomName = parts[1].trim();
                String roomDesc = parts[2].trim();

                Room room = new Room(roomNumber, roomName, roomDesc);

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

    public static void loadItems(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] parts = line.split(",", 3);
                int roomNumber = Integer.parseInt(parts[0].trim());
                String itemName = parts[1].trim();
                String itemDescription = parts[2].trim();
                Item item = new Item(itemName, itemDescription, roomNumber);
                items.put(itemName.toLowerCase(), item);
            }
        } catch (IOException e) {
            System.out.println("Error loading items: " + e.getMessage());
        }
    }

    public static void loadPuzzles(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] parts = line.split(",", 4);
                int roomNumber = Integer.parseInt(parts[0].trim());
                String description = parts[1].trim();
                String answer = parts[2].trim();
                int attempts = Integer.parseInt(parts[3].trim());
                puzzles.put(roomNumber, new Puzzle(description, answer, attempts, roomNumber));
            }
        } catch (IOException e) {
            System.out.println("Error loading puzzles: " + e.getMessage());
        }
    }
}
