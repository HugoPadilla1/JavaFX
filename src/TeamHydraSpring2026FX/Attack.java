package TeamHydraSpring2026FX;

/*
Attack class coders: Samuel Michel
This class helps create unique attacks for each monster.
 */
public class Attack {
    private int attackID;
    private String attackName;
    private double damageMultiplier;
    private String statusEffect;
    private double statusChance;
    private String flavorText;

    public Attack(int attackID, String attackName, double damageMultiplier, String statusEffect, double statusChance) {
        this(attackID, attackName, damageMultiplier, statusEffect, statusChance, "");
    }

    public Attack(int attackID, String attackName, double damageMultiplier, String statusEffect, double statusChance, String flavorText) {
        this.attackID = attackID;
        this.attackName = attackName;
        this.damageMultiplier = damageMultiplier;
        this.statusEffect = statusEffect;
        this.statusChance = statusChance;
        this.flavorText = flavorText;
    }

    public int calculateDamage(Entity attacker, Entity defender) {
        if (attacker == null || defender == null) {
            return 0;
        }
        int damage = (int) Math.round(damageMultiplier * attacker.getDamage()) - defender.getDefense();
        return Math.max(0, damage);
    }

    public int heal(Entity defender) {
        if (defender == null) return 0;
        int amount = Math.max(5, (int) Math.round(damageMultiplier * 10));
        defender.setHealth(defender.getHealth() + amount);
        return amount;
    }

    public int addDefense(Entity defender) {
        if (defender == null) return 0;
        int amount = Math.max(2, (int) Math.round(damageMultiplier * 5));
        defender.setDefense(defender.getDefense() + amount);
        return amount;
    }

    public void paralyze(Entity defender) {
        if (defender != null) defender.setSpeed(Math.max(0, defender.getSpeed() - 2));
    }

    public void poison(Entity defender) {
        if (defender != null) defender.setHealth(Math.max(0, defender.getHealth() - 5));
    }

    public int special1(Entity attacker, Entity defender) {
        return calculateDamage(attacker, defender);
    }

    public int special2(Entity attacker, Entity defender) {
        return calculateDamage(attacker, defender);
    }

    public int special3(Entity attacker, Entity defender) {
        return calculateDamage(attacker, defender);
    }

    public int special4(Entity attacker, Entity defender) {
        return calculateDamage(attacker, defender);
    }

    public int getAttackID() {
        return attackID;
    }

    public String getAttackName() {
        return attackName;
    }

    public double getDamageMultiplier() {
        return damageMultiplier;
    }

    public String getStatusEffect() {
        return statusEffect;
    }

    public double getStatusChance() {
        return statusChance;
    }

    public String getFlavorText() {
        return flavorText;
    }

    @Override
    public String toString() {
        return attackName + " [x" + damageMultiplier + ", status=" + statusEffect + "]";
    }
}
