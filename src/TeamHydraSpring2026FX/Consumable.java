package TeamHydraSpring2026FX;

public class Consumable extends Items {
    private int addHealth;
    private int removeHealth;
    private int addSpeed;
    private int removeSpeed;
    private int addDefense;
    private int removeDefense;
    private String status;
    private int effectDuration;

    public Consumable(int itemID, String itemName, String itemDescription, boolean droppable, int dropChance, Room location, int addHealth, int removeHealth, int addSpeed, int removeSpeed, int addDefense, int removeDefense, String status, int effectDuration) {
        super(itemID, itemName, itemDescription, droppable, dropChance, location);
        this.addHealth = addHealth;
        this.removeHealth = removeHealth;
        this.addSpeed = addSpeed;
        this.removeSpeed = removeSpeed;
        this.addDefense = addDefense;
        this.removeDefense = removeDefense;
        this.status = status;
        this.effectDuration = effectDuration;
    }

    public int getAddHealth() {
        return addHealth;
    }

    public void setAddHealth(int addHealth) {
        this.addHealth = addHealth;
    }

    public int getRemoveHealth() {
        return removeHealth;
    }

    public void setRemoveHealth(int removeHealth) {
        this.removeHealth = removeHealth;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getEffectDuration() {
        return effectDuration;
    }

    public void setEffectDuration(int effectDuration) {
        this.effectDuration = effectDuration;
    }

    public void affectHealth(Player user) {
        if (user != null) {
            user.setHealth(user.getHealth() + addHealth);
            if (user.getHealth() < 0) {
                user.setHealth(0);
            }
        }
    }

    public void affectHealth(Monsters enemy) {
        if (enemy != null) {
            enemy.setHealth(enemy.getHealth() + addHealth);
            if (enemy.getHealth() < 0) {
                enemy.setHealth(0);
            }
        }
    }

    public void affectSpeed(Player user) {
        if (user != null) {
            user.setSpeed(user.getSpeed() + addSpeed);
            if (user.getSpeed() < 0) {
                user.setSpeed(0);
            }
        }
    }

    public void affectSpeed(Monsters enemy) {
        if (enemy != null) {
            enemy.setSpeed(enemy.getSpeed() + addSpeed);
            if (enemy.getSpeed() < 0) {
                enemy.setSpeed(0);
            }
        }
    }

    public void affectDefense(Player user) {
        if (user != null) {
            user.setDefense(user.getDefense() + addDefense);
            if (user.getDefense() < 0) {
                user.setDefense(0);
            }
        }
    }

    public void affectDefense(Monsters enemy) {
        if (enemy != null) {
            enemy.setDefense(enemy.getDefense() + addDefense);
            if (enemy.getDefense() < 0) {
                enemy.setDefense(0);
            }
        }
    }

    public void applyStatus(Player user) {
        if (user != null) {
            System.out.println(user + " is affected by status: " + status +
                    " for " + effectDuration + " turn(s).");
        }
    }

    public void applyStatus(Monsters enemy) {
        if (enemy != null) {
            System.out.println(enemy + " is affected by status: " + status +
                    " for " + effectDuration + " turn(s).");
        }
    }

    public void affectEntity(Player user) {
        if (user != null) {
            affectHealth(user);
            affectSpeed(user);
            affectDefense(user);
            applyStatus(user);
        }
    }

    public void affectEntity(Monsters enemy) {
        if (enemy != null) {
            affectHealth(enemy);
            affectSpeed(enemy);
            affectDefense(enemy);
            applyStatus(enemy);
        }
    }

    public void removeEffects(Player user) {
        if (user != null) {
            user.setHealth(Math.max(0, user.getHealth() - removeHealth));
            user.setSpeed(Math.max(0, user.getSpeed() - removeSpeed));
            user.setDefense(Math.max(0, user.getDefense() - removeDefense));
        }
    }

    public void removeEffects(Monsters enemy) {
        if (enemy != null) {
            enemy.setHealth(Math.max(0, enemy.getHealth() - removeHealth));
            enemy.setSpeed(Math.max(0, enemy.getSpeed() - removeSpeed));
            enemy.setDefense(Math.max(0, enemy.getDefense() - removeDefense));
        }
    }

    @Override
    public String toString() {
        return getItemName() + " - " + getItemDescription() +
                " [Health: " + (addHealth >= 0 ? "+" : "") + addHealth +
                ", Speed: " + (addSpeed >= 0 ? "+" : "") + addSpeed +
                ", Defense: " + (addDefense >= 0 ? "+" : "") + addDefense +
                ", Status: " + status +
                ", Duration: " + effectDuration + "]";
    }
}
