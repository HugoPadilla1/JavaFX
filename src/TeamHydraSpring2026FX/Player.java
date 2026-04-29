package TeamHydraSpring2026FX;
import java.io.File;
import java.util.ArrayList;

/*
Player class editors: Christopher Young, Samuel Michel
 */
public class Player extends Entity {
    private String weapon;
    private Weapon equippedWeapon;
    private int inventorySpace;
    private Room location;
    private ArrayList<Items> inventory;

    public Player() {
        weapon = "";
        equippedWeapon = null;
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
        System.out.println("Commands: move, explore, grab, drop, examine, inventory, use, equip, fight, puzzle, rest, status, quit");
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

    public String equipItem(Items desiredItem, ArrayList<Items> playerInventory) {
        if (desiredItem == null || playerInventory == null || !playerInventory.contains(desiredItem)) {
            return "Item not found.";
        }

        if (desiredItem instanceof Weapon) {
            return equipWeapon((Weapon) desiredItem, playerInventory);
        } else if (desiredItem instanceof Wearable) {
            ((Wearable) desiredItem).affectEntity(this);
            return "Wearable equipped: " + desiredItem.getItemName() + ".";
        } else {
            return "That item cannot be equipped.";
        }
    }

    public String equipWeapon(Weapon newWeapon, ArrayList<Items> playerInventory) {
        if (newWeapon == null || playerInventory == null || !playerInventory.contains(newWeapon)) {
            return "Weapon not found in inventory.";
        }

        if (equippedWeapon != null) {
            equippedWeapon.removeEntityDamage(this);
            equippedWeapon.removeEntityHP(this);
            playerInventory.add(equippedWeapon);
        }

        playerInventory.remove(newWeapon);
        equippedWeapon = newWeapon;
        weapon = newWeapon.getItemName();
        newWeapon.affectEntity(this);
        return "Weapon equipped: " + newWeapon.getItemName() + ".";
    }

    public String unequipWeapon() {
        if (equippedWeapon == null) {
            return "No weapon is currently equipped.";
        }

        Weapon oldWeapon = equippedWeapon;
        oldWeapon.removeEntityDamage(this);
        oldWeapon.removeEntityHP(this);
        inventory.add(oldWeapon);
        equippedWeapon = null;
        weapon = "";
        return "Weapon unequipped: " + oldWeapon.getItemName() + ".";
    }

    public String examineItem(Items item) {
        if (item == null) {
            return "No item selected.";
        }

        StringBuilder details = new StringBuilder();
        details.append(item.getItemName()).append("\n");
        details.append(item.getItemDescription()).append("\n");
        details.append("Type: ").append(item.getClass().getSimpleName()).append("\n");

        if (item instanceof Weapon) {
            Weapon w = (Weapon) item;
            details.append("Attack Bonus: +").append(w.getDamage()).append("\n");
            if (w.getAddedHealth() != 0) {
                details.append("Health Bonus: +").append(w.getAddedHealth()).append("\n");
            }
        } else if (item instanceof Wearable) {
            Wearable wearable = (Wearable) item;
            details.append("Gear Type: ").append(wearable.getType()).append("\n");
            details.append("Health Bonus: +").append(wearable.getAddMaxHealth()).append("\n");
            details.append("Defense Bonus: +").append(wearable.getAddDefense()).append("\n");
            details.append("Speed Bonus: +").append(wearable.getAddSpeed()).append("\n");
        } else if (item instanceof Consumable) {
            Consumable c = (Consumable) item;
            details.append("Health Effect: ").append(c.getAddHealth() - c.getRemoveHealth()).append("\n");
            details.append("Defense Effect: ").append(c.getAddDefense() - c.getRemoveDefense()).append("\n");
            details.append("Speed Effect: ").append(c.getAddSpeed() - c.getRemoveSpeed()).append("\n");
            details.append("Status: ").append(c.getStatus()).append("\n");
            details.append("Duration: ").append(c.getEffectDuration()).append(" turn(s)\n");
        } else if (item instanceof KeyItem) {
            KeyItem key = (KeyItem) item;
            details.append("Key Type: ").append(key.getKeyType()).append("\n");
            details.append("Linked Puzzle ID: ").append(key.getLinkedPuzzleID()).append("\n");
        }

        details.append("Droppable: ").append(item.getDroppable() ? "Yes" : "No");
        return details.toString();
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

    public Weapon getEquippedWeapon() {
        return equippedWeapon;
    }

    public void setEquippedWeaponDirect(Weapon equippedWeapon) {
        this.equippedWeapon = equippedWeapon;
        this.weapon = equippedWeapon == null ? "" : equippedWeapon.getItemName();
    }
}
