package textgame_update;

public class Item {
    private String name;
    private String description;
    private int roomNumber;
    private boolean inInventory;

    public Item(String name, String description, int roomNumber) {
        this.name = name;
        this.description = description;
        this.roomNumber = roomNumber;
        this.inInventory = false;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public boolean isInInventory() {
        return inInventory;
    }

    public boolean isInRoom(int roomNumber) {
        return !inInventory && this.roomNumber == roomNumber;
    }

    public void pickUp() {
        inInventory = true;
        roomNumber = -1;
    }

    public void drop(int roomNumber) {
        inInventory = false;
        this.roomNumber = roomNumber;
    }
}
