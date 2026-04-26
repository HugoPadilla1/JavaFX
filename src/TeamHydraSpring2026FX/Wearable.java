package TeamHydraSpring2026FX;
public class Wearable extends Items {
    private String type;
    private int addMaxHealth;
    private int removeMaxHealth;
    private int addDefense;
    private int removeDefense;
    private int addSpeed;
    private int removeSpeed;

    public Wearable(int itemID, String itemName, String itemDescription, boolean droppable, int dropChance, Room location, String type, int addMaxHealth, int removeMaxHealth, int addDefense, int removeDefense, int addSpeed, int removeSpeed) {
        super(itemID, itemName, itemDescription, droppable, dropChance, location);
        this.type = type;
        this.addMaxHealth = addMaxHealth;
        this.removeMaxHealth = removeMaxHealth;
        this.addDefense = addDefense;
        this.removeDefense = removeDefense;
        this.addSpeed = addSpeed;
        this.removeSpeed = removeSpeed;
    }

    public void editMaxHealth(Player user) {
        if (user != null) {
            user.setHealth(user.getHealth() + addMaxHealth);
        }
    }

    public void removeMaxHealth(Player user) {
        if (user != null) {
            user.setHealth(user.getHealth() - removeMaxHealth);

            if (user.getHealth() < 0) {
                user.setHealth(0);
            }
        }
    }

    public void editDefense(Player user) {
        if (user != null) {
            user.setDefense(user.getDefense() + addDefense);
        }
    }

    // Remove defense bonus from wearable
    public void removeDefense(Player user) {
        if (user != null) {
            user.setDefense(user.getDefense() - removeDefense);

            if (user.getDefense() < 0) {
                user.setDefense(0);
            }
        }
    }

    public void editSpeed(Player user) {
        if (user != null) {
            user.setSpeed(user.getSpeed() + addSpeed);
        }
    }

    // Remove speed bonus from wearable
    public void removeSpeed(Player user) {
        if (user != null) {
            user.setSpeed(user.getSpeed() - removeSpeed);

            if (user.getSpeed() < 0) {
                user.setSpeed(0);
            }
        }
    }

    public void affectEntity(Player user) {
        if (user != null) {
            editMaxHealth(user);
            editDefense(user);
            editSpeed(user);
        }
    }

    // Remove all wearable bonuses at once
    public void removeEffects(Player user) {
        if (user != null) {
            removeMaxHealth(user);
            removeDefense(user);
            removeSpeed(user);
        }
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getAddMaxHealth() {
        return addMaxHealth;
    }

    public void setAddMaxHealth(int addMaxHealth) {
        this.addMaxHealth = addMaxHealth;
    }

    public int getRemoveMaxHealth() {
        return removeMaxHealth;
    }

    public void setRemoveMaxHealth(int removeMaxHealth) {
        this.removeMaxHealth = removeMaxHealth;
    }

    public int getAddDefense() {
        return addDefense;
    }

    public void setAddDefense(int addDefense) {
        this.addDefense = addDefense;
    }

    public int getRemoveDefense() {
        return removeDefense;
    }

    public void setRemoveDefense(int removeDefense) {
        this.removeDefense = removeDefense;
    }

    public int getAddSpeed() {
        return addSpeed;
    }

    public void setAddSpeed(int addSpeed) {
        this.addSpeed = addSpeed;
    }

    public int getRemoveSpeed() {
        return removeSpeed;
    }

    public void setRemoveSpeed(int removeSpeed) {
        this.removeSpeed = removeSpeed;
    }

    @Override
    public String toString() {
        return getItemName() + " - " + getItemDescription() +
                " [Type: " + type +
                ", Health: +" + addMaxHealth +
                ", Defense: +" + addDefense +
                ", Speed: +" + addSpeed + "]";
    }
}
