package TeamHydraSpring2026FX;
public class KeyItem extends Items {

    private String keyType;
    private int linkedPuzzleID;
    private boolean consumedOnUse;
    private boolean usable;

    public KeyItem(int itemID, String itemName, String itemDescription, boolean droppable, int dropChance, Room location, String keyType, int linkedPuzzleID, boolean consumedOnUse, boolean usable) {
        super(itemID, itemName, itemDescription, droppable, dropChance, location);
        this.keyType = keyType;
        this.linkedPuzzleID = linkedPuzzleID;
        this.consumedOnUse = consumedOnUse;
        this.usable = usable;
    }


    public void itemEquipEffect(Player user) {
        if (user != null) {
            System.out.println(getItemName() + " is now ready to use.");
        }
    }

    public void itemUnequipEffect(Player user) {
        if (user != null) {
            System.out.println(getItemName() + " is no longer active.");
        }
    }

    public boolean matchesPuzzle(Puzzle puzzle) {
        if (puzzle == null) {
            return false;
        }
        return this.linkedPuzzleID == puzzle.getPuzzleID();
    }

    public void useOnPuzzle(Puzzle puzzle, Player user) {
        if (puzzle == null || user == null) {
            return;
        }

        if (!usable) {
            System.out.println(getItemName() + " cannot be used right now.");
            return;
        }

        if (matchesPuzzle(puzzle)) {
            System.out.println(getItemName() + " can be used for this puzzle.");
        } else {
            System.out.println(getItemName() + " does not seem to work here.");
        }
    }

    public String getKeyType() {
        return keyType;
    }

    public void setKeyType(String keyType) {
        this.keyType = keyType;
    }

    public int getLinkedPuzzleID() {
        return linkedPuzzleID;
    }

    public void setLinkedPuzzleID(int linkedPuzzleID) {
        this.linkedPuzzleID = linkedPuzzleID;
    }

    public boolean isConsumedOnUse() {
        return consumedOnUse;
    }

    public void setConsumedOnUse(boolean consumedOnUse) {
        this.consumedOnUse = consumedOnUse;
    }

    public boolean isUsable() {
        return usable;
    }

    public void setUsable(boolean usable) {
        this.usable = usable;
    }

    @Override
    public String toString() {
        return getItemName() + " - " + getItemDescription() +
                " [Type: " + keyType +
                ", Linked Puzzle ID: " + linkedPuzzleID +
                ", Usable: " + usable +
                ", Consumed On Use: " + consumedOnUse + "]";
    }
}
