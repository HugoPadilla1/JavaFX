package TextBasedGame;
import java.util.HashMap;
import java.util.Map;

public class Room {
    int roomNumber;
    String name;
    String description;
    boolean visited;
    Map<String, Integer> exits;  // Direction → RoomNumber

    // Constructor
    public Room(int roomNumber, String name, String description) {
        this.roomNumber = roomNumber;
        this.name = name;
        this.description = description;
        this.visited = false;
        this.exits = new HashMap<>();
    }

    // Add an exit
    public void addExit(String direction, int roomNumber) {
        exits.put(direction.toUpperCase(), roomNumber);
    }

    // Mark as visited
    public void visit() {
        visited = true;
    }

    // Get room info
    public String getInfo() {
        String info = "== " + name + " ==\n" + description + "\n";
        if (visited) {
            info += "(You have visited this room before.)\n";
        }
        return info;
    }
}