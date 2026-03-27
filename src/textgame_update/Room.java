package textgame_update;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Room {
    private int roomNumber;
    private String name;
    private String description;
    private boolean visited;
    private Map<String, Integer> exits;

    public Room(int roomNumber, String name, String description) {
        this.roomNumber = roomNumber;
        this.name = name;
        this.description = description;
        this.visited = false;
        this.exits = new HashMap<>();
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public String getName() {
        return name;
    }

    public Map<String, Integer> getExits() {
        return exits;
    }

    public void addExit(String direction, int roomNumber) {
        exits.put(direction.toUpperCase(), roomNumber);
    }

    public boolean hasExit(String direction) {
        return exits.containsKey(direction.toUpperCase());
    }

    public int getExitRoom(String direction) {
        return exits.get(direction.toUpperCase());
    }

    public void visit() {
        visited = true;
    }

    public String getInfo() {
        String info = "== " + name + " ==\n" + description + "\n";
        if (visited) {
            info += "(You have visited this room before.)\n";
        }
        return info;
    }

    public List<Item> getItemsInRoom(Map<String, Item> allItems) {
        ArrayList<Item> roomItems = new ArrayList<>();
        for (Item item : allItems.values()) {
            if (item.isInRoom(roomNumber)) {
                roomItems.add(item);
            }
        }
        return roomItems;
    }

    public Item findItemInRoom(String itemName, Map<String, Item> allItems) {
        for (Item item : allItems.values()) {
            if (item.isInRoom(roomNumber) && item.getName().equalsIgnoreCase(itemName)) {
                return item;
            }
        }
        return null;
    }

    public boolean hasUnsolvedPuzzle(Map<Integer, Puzzle> puzzles) {
        Puzzle puzzle = puzzles.get(roomNumber);
        return puzzle != null && !puzzle.isSolved();
    }

    public Puzzle getPuzzle(Map<Integer, Puzzle> puzzles) {
        return puzzles.get(roomNumber);
    }
}
