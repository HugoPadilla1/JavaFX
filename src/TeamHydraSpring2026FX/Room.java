package TeamHydraSpring2026FX;
import java.util.HashMap;

/*
Room class coders: Joshua Lingerfelt, Samuel Michel
 */

public class Room {
    private String roomID;
    private String roomName;
    private String roomDescription;
    private HashMap<String, String> doorDescriptions;
    private HashMap<String, String> exits;
    private HashMap<String, Items> items;
    private HashMap<String, Puzzle> puzzles;
    private HashMap<String, Monsters> monsters;
    private boolean isSafeRoom;
    private boolean isVisited;

    public Room(String roomID, String roomName, String roomDescription, boolean isSafeRoom) {
        this.roomID = roomID;
        this.roomName = roomName;
        this.roomDescription = roomDescription;
        this.isSafeRoom = isSafeRoom;
        this.isVisited = false;
        this.doorDescriptions = new HashMap<>();
        this.exits = new HashMap<>();
        this.items = new HashMap<>();
        this.puzzles = new HashMap<>();
        this.monsters = new HashMap<>();
    }

    // Getters and setters
    public String getRoomID() {
        return roomID;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getRoomDescription() {
        return roomDescription;
    }

    public void setRoomDescription(String description) {
        this.roomDescription = description;
    }

    public boolean isSafeRoom() {
        return isSafeRoom;
    }

    public void setSafeRoom(boolean isSafeRoom) {
        this.isSafeRoom = isSafeRoom;
    }

    public boolean isVisited() {
        return isVisited;
    }

    public void setVisited(boolean visited) {
        this.isVisited = visited;
    }

    public HashMap<String, String> getDoorDescriptions() {
        return doorDescriptions;
    }

    public HashMap<String, String> getExits() {
        return exits;
    }

    public HashMap<String, Items> getItems() {
        return items;
    }

    public HashMap<String, Puzzle> getPuzzles() {
        return puzzles;
    }

    public HashMap<String, Monsters> getMonsters() {
        return monsters;
    }

    // Doors and exits
    public void addDoorDescription(String direction, String description) {
        doorDescriptions.put(direction.toUpperCase(), description);
    }

    public void addExit(String direction, String destinationID) {
        exits.put(direction.toUpperCase(), destinationID);
    }

    public String getExit(String direction) {
        return exits.get(direction.toUpperCase());
    }

    public boolean hasExit(String direction) {
        return exits.containsKey(direction.toUpperCase());
    }

    // Display methods
    public void displayRoomEntry() {
        System.out.println("=== " + roomName + " ===");
        System.out.println(roomDescription);
        if (isSafeRoom) {
            System.out.println("[This is a safe room. You may REST or SAVE here.]");
        }
    }

    public void displayExplore(GameMap gameMap) {
        System.out.println("-- Exits --");
        if (doorDescriptions.isEmpty()) {
            System.out.println("  No visible exits.");
        } else {
            for (HashMap.Entry<String, String> entry : doorDescriptions.entrySet()) {
                String direction = entry.getKey();
                String description = entry.getValue();

                // Get the destination room and check if it's visited
                String destinationID = exits.get(direction);
                Room destinationRoom = gameMap.getRoom(destinationID);

                if (destinationRoom != null && destinationRoom.isVisited()) {
                    System.out.println("  [" + direction + "] " + description + " Leads to: " + destinationRoom.getRoomName());
                } else {
                    System.out.println("  [" + direction + "] " + description);
                }
            }
        }

        System.out.println("-- Items --");
        if (items.isEmpty()) {
            System.out.println("  Nothing of interest.");
        } else {
            for (Items item : items.values()) {
                System.out.println("  " + item);
            }
        }

        System.out.println("-- Puzzles --");
        if (puzzles.isEmpty()) {
            System.out.println("  No puzzles here.");
        } else {
            for (Puzzle puzzle : puzzles.values()) {
                System.out.println("  " + puzzle);
            }
        }
    }

}
