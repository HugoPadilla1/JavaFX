package TeamHydraSpring2026FX;
public class Items {
    private int itemID;
    private String itemName;
    private String itemDescription;
    private boolean droppable;
    private int dropChance;
    private Room location;

    public Items(int itemID, String itemName, String itemDescription, boolean droppable, int dropChance, Room location) {
        this.itemID = itemID;
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.droppable = droppable;
        this.dropChance = dropChance;
        this.location = location;
    }

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public boolean getDroppable() {
        return droppable;
    }

    public void setDroppable(boolean droppable) {
        this.droppable = droppable;
    }

    public int getDropChance() {
        return dropChance;
    }

    public void setDropChance(int dropChance) {
        this.dropChance = dropChance;
    }

    public Room getLocation() {
        return location;
    }

    public void setLocation(Room location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return itemName + " - " + itemDescription;
    }
}
