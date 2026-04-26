package TeamHydraSpring2026FX;
import java.io.File;
import java.util.ArrayList;

/*
Player class editors: Christopher Young, Samuel Michel
 */
public class Player extends Entity {
    private String weapon;
    private int inventorySpace;
    private Room location;
    private ArrayList<Items> inventory;

    public Player() {
        weapon = "";
        inventorySpace = 10;
        location = null;
        inventory = new ArrayList<>();
        setHealth(100);
        setDamage(10);
        setDefense(5);
        setSpeed(5);
    }

    public boolean startGame(File newGame) {
        System.out.println("New game started.");
        return true;
    }

    public boolean loadGame(File saveFile) {
        System.out.println("Loading game...");
        return true;
    }

    public boolean saveGame(File currentData) {
        System.out.println("Game saved.");
        return true;
    }

    public boolean quitGame() {
        System.out.println("Quitting game...");
        return false;
    }

    public void viewTutorial() {
        System.out.println("Commands: move, explore, grab, drop, inventory, use, equip, fight, puzzle, rest, status, quit");
    }

    public void viewStatus() {
        System.out.println("Health: " + getHealth());
        System.out.println("Damage: " + getDamage());
        System.out.println("Defense: " + getDefense());
        System.out.println("Speed: " + getSpeed());
        System.out.println("Weapon: " + (weapon == null || weapon.isEmpty() ? "None" : weapon));
        System.out.println("Inventory: " + inventory.size() + "/" + inventorySpace);
    }

    public void displayMap(Room currentRoom) {
        if (currentRoom != null) {
            System.out.println("You are in: " + currentRoom.getRoomName());
        }
    }

    public void rest(Room currentRoom) {
        if (currentRoom != null && currentRoom.isSafeRoom()) {
            setHealth(Math.min(100, getHealth() + 25));
            System.out.println("You rested and recovered health.");
        } else {
            setHealth(Math.min(100, getHealth() + 10));
            System.out.println("You catch your breath and recover a little health.");
        }
    }

    public void useItem(Items desiredItem, ArrayList<Items> playerInventory) {
        if (desiredItem == null || playerInventory == null || !playerInventory.contains(desiredItem)) {
            System.out.println("Item not found.");
            return;
        }

        if (desiredItem instanceof Consumable) {
            Consumable c = (Consumable) desiredItem;
            c.affectEntity(this);
            playerInventory.remove(desiredItem);
            System.out.println("Consumable used: " + desiredItem.getItemName());
        } else if (desiredItem instanceof KeyItem) {
            ((KeyItem) desiredItem).itemEquipEffect(this);
        } else {
            System.out.println("That item cannot be used directly.");
        }
    }

    public void equipItem(Items desiredItem, ArrayList<Items> playerInventory) {
        if (desiredItem == null || playerInventory == null || !playerInventory.contains(desiredItem)) {
            System.out.println("Item not found.");
            return;
        }

        if (desiredItem instanceof Weapon) {
            Weapon w = (Weapon) desiredItem;
            weapon = w.getItemName();
            w.affectEntity(this);
            System.out.println("Weapon equipped: " + w.getItemName());
        } else if (desiredItem instanceof Wearable) {
            ((Wearable) desiredItem).affectEntity(this);
            System.out.println("Wearable equipped: " + desiredItem.getItemName());
        } else {
            System.out.println("That item cannot be equipped.");
        }
    }

    public int attack(int attack) {
        return getDamage() + attack;
    }

    public int attack() {
        return getDamage();
    }

    public int defend(int defense) {
        return getDefense() + defense;
    }

    public void dropItem(Player user, Room currentRoom) {
        if (inventory.size() > 0) {
            Items item = inventory.remove(0);
            if (currentRoom != null) currentRoom.getItems().put(item.getItemName().toUpperCase(), item);
            System.out.println("Dropped: " + item.getItemName());
        } else {
            System.out.println("Inventory empty.");
        }
    }

    public void grabItem(Player user, Room currentRoom) {
        System.out.println("Use GRAB itemID in GameTester to pick up a specific item.");
    }

    public ArrayList<Items> getInventory() {
        return inventory;
    }

    public int getInventorySpace() {
        return inventorySpace;
    }

    public void setInventorySpace(int inventorySpace) {
        this.inventorySpace = inventorySpace;
    }

    public Room getLocation() {
        return location;
    }

    public void setLocation(Room location) {
        this.location = location;
    }

    public String getWeapon() {
        return weapon;
    }
}
