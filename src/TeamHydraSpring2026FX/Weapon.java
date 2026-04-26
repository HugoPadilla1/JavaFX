package TeamHydraSpring2026FX;
public class Weapon extends Items {
    private int damage;
    private int addedHealth;

    public Weapon(int itemID, String itemName, String itemDescription, boolean droppable, int dropChance, Room location, int damage, int addedHealth) {
        super(itemID, itemName, itemDescription, droppable, dropChance, location);
        this.damage = damage;
        this.addedHealth = addedHealth;
    }

    // Apply weapon effects to a player (e.g., when equipped)
    public void addEntityDamage(Player user) {
        if (user != null) {
            user.setDamage(user.getDamage() + damage);
        }
    }

    public void removeEntityDamage(Player user) {
        if (user != null) {
            user.setDamage(user.getDamage() - damage);
        }
    }

    public void addEntityHP(Player user) {
        if (user != null) {
            user.setHealth(user.getHealth() + addedHealth);
        }
    }

    public void removeEntityHP(Player user) {
        if (user != null) {
            user.setHealth(user.getHealth() - addedHealth);

            if (user.getHealth() <= 0) {
                user.setHealth(0);
            }
        }
    }

    // Affect a player or monster (overloads)
    public void affectEntity(Player user) {
        if (user != null) {
            addEntityDamage(user);
            addEntityHP(user);
        }
    }

    public void affectEntity(Monsters enemy) {
        if (enemy != null) {
            enemy.setHealth(enemy.getHealth() - damage);

            if (enemy.getHealth() <= 0) {
                enemy.setHealth(0);
            }
        }
    }

    //GETTERS AND SETTERS
    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getAddedHealth() {
        return addedHealth;
    }

    public void setAddedHealth(int addedHealth) {
        this.addedHealth = addedHealth;
    }

    @Override
    public String toString() {
        return "Weapon{" +
                "damage=" + damage +
                ", addedHealth=" + addedHealth +
                '}';
    }
}
