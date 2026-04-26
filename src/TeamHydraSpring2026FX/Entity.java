package TeamHydraSpring2026FX;
/*
Entity class coders: Samuel Michel

The entity class is the parent class of the Player and Monster classes.
 */
public class Entity {
    private int health;
    private int damage;
    private int defense;
    private int speed;


    // Getters and setters for basic stats
    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
}
