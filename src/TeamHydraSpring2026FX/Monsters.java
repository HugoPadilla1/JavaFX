package TeamHydraSpring2026FX;
import java.util.ArrayList;

/*
Monsters class coders: Samuel Michel
 */
public class Monsters extends Entity {
    private int monsterID;
    private String monsterName;
    private String monsterDescription;
    private String status;
    private boolean isBoss;
    private ArrayList<Items> monsterInventory;
    private ArrayList<Attack> monsterAttackAL;
    private Room monsterRoom;

    public Monsters(int monsterID, String monsterName, String monsterDescription, boolean isBoss,
                    ArrayList<Items> monsterInventory, Room monsterRoom, ArrayList<Attack> monsterAttackAL) {
        this.monsterID = monsterID;
        this.monsterName = monsterName;
        this.monsterDescription = monsterDescription;
        this.isBoss = isBoss;
        this.monsterInventory = monsterInventory == null ? new ArrayList<>() : monsterInventory;
        this.monsterRoom = monsterRoom;
        this.monsterAttackAL = monsterAttackAL == null ? new ArrayList<>() : monsterAttackAL;
        this.status = "none";
    }

    public Monsters(int monsterID, String monsterName, String monsterDescription, boolean isBoss,
                    int hp, int atk, int def, int spd) {
        this.monsterID = monsterID;
        this.monsterName = monsterName;
        this.monsterDescription = monsterDescription;
        this.isBoss = isBoss;
        this.status = "none";
        setHealth(hp);
        setDamage(atk);
        setDefense(def);
        setSpeed(spd);
        this.monsterInventory = new ArrayList<>();
        this.monsterAttackAL = new ArrayList<>();
        this.monsterRoom = null;
    }

    public Monsters(int monsterID, String monsterName, String monsterDescription, String status, boolean isBoss,
                    int hp, int atk, int def, int spd) {
        this(monsterID, monsterName, monsterDescription, isBoss, hp, atk, def, spd);
        this.status = status;
    }

    public Attack spinMonsterAttack() {
        if (monsterAttackAL == null || monsterAttackAL.isEmpty()) {
            return new Attack(0, "Basic Attack", 1.0, "none", 0, monsterName + " attacks!");
        }
        int randInt = (int) (Math.random() * monsterAttackAL.size());
        return monsterAttackAL.get(randInt);
    }

    public int getMonsterID() {
        return monsterID;
    }

    public void setMonsterID(int monsterID) {
        this.monsterID = monsterID;
    }

    public String getMonsterName() {
        return monsterName;
    }

    public void setMonsterName(String monsterName) {
        this.monsterName = monsterName;
    }

    public String getMonsterDescription() {
        return monsterDescription;
    }

    public void setMonsterDescription(String monsterDescription) {
        this.monsterDescription = monsterDescription;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isBoss() {
        return isBoss;
    }

    public void setBoss(boolean boss) {
        isBoss = boss;
    }

    public ArrayList<Items> getMonsterInventory() {
        return monsterInventory;
    }

    public void setMonsterInventory(ArrayList<Items> monsterInventory) {
        this.monsterInventory = monsterInventory;
    }

    public Attack getRandomAttack(int randInt) {
        if (monsterAttackAL == null || monsterAttackAL.isEmpty()) {
            return spinMonsterAttack();
        }
        return monsterAttackAL.get(Math.max(0, Math.min(randInt, monsterAttackAL.size() - 1)));
    }

    public ArrayList<Attack> getMonsterAttackAL() {
        return monsterAttackAL;
    }

    public void setMonsterAttackAL(ArrayList<Attack> monsterAttackAL) {
        this.monsterAttackAL = monsterAttackAL;
    }

    public Room getMonsterRoom() {
        return monsterRoom;
    }

    public void setMonsterRoom(Room monsterRoom) {
        this.monsterRoom = monsterRoom;
    }

    @Override
    public String toString() {
        return monsterName + " [HP=" + getHealth() + ", ATK=" + getDamage() +
                ", DEF=" + getDefense() + ", SPD=" + getSpeed() +
                (isBoss ? ", BOSS" : "") + "]";
    }
}
