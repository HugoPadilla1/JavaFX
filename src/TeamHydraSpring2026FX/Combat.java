package TeamHydraSpring2026FX;

import java.util.ArrayList;

/**
 * Object-oriented combat engine.
 *
 * The JavaFX controller should not calculate damage, run monster turns, award drops,
 * or decide victory/death. It should only call these methods and display the returned messages.
 */
public class Combat {
    private Player player;
    private Monsters enemy;
    private Room room;
    private boolean active;
    private boolean defending;
    private boolean fled;

    public Combat() {
        active = false;
        defending = false;
        fled = false;
    }

    public Combat(Player player, Monsters enemy, Room room) {
        start(player, enemy, room);
    }

    public ArrayList<String> start(Player player, Monsters enemy, Room room) {
        ArrayList<String> messages = new ArrayList<>();
        this.player = player;
        this.enemy = enemy;
        this.room = room;
        this.defending = false;
        this.fled = false;
        this.active = player != null && enemy != null && player.getHealth() > 0 && enemy.getHealth() > 0;

        if (!active) {
            messages.add("Combat could not start.");
            return messages;
        }

        messages.add("========== COMBAT: " + enemy.getMonsterName() + " ==========");
        messages.add(enemy.getMonsterDescription());
        messages.add("Enemy HP: " + enemy.getHealth());
        return messages;
    }

    public ArrayList<String> playerAttack() {
        ArrayList<String> messages = new ArrayList<>();
        if (!canAct(messages)) return messages;

        int damage = Math.max(0, player.attack() - enemy.getDefense());
        enemy.setHealth(Math.max(0, enemy.getHealth() - damage));
        messages.add("You strike " + enemy.getMonsterName() + " for " + damage + " damage.");

        if (enemy.getHealth() <= 0) {
            messages.addAll(defeatEnemy());
            return messages;
        }

        messages.addAll(monsterTurn());
        return messages;
    }

    public ArrayList<String> playerDefend() {
        ArrayList<String> messages = new ArrayList<>();
        if (!canAct(messages)) return messages;

        defending = true;
        messages.add("You brace for impact. Incoming attack damage is reduced.");
        messages.addAll(monsterTurn());
        return messages;
    }

    public ArrayList<String> playerUseConsumable(Items item) {
        ArrayList<String> messages = new ArrayList<>();
        if (!canAct(messages)) return messages;

        if (!(item instanceof Consumable)) {
            messages.add("That item cannot be used in combat.");
            return messages;
        }

        player.useItem(item, player.getInventory());
        messages.add("You used " + item.getItemName() + ".");
        messages.addAll(monsterTurn());
        return messages;
    }

    public ArrayList<String> tryFlee() {
        ArrayList<String> messages = new ArrayList<>();
        if (!canAct(messages)) return messages;

        int chance = 50 + player.getSpeed() - enemy.getSpeed();
        chance = Math.max(10, Math.min(90, chance));
        int roll = (int) (Math.random() * 100);

        if (roll < chance) {
            active = false;
            fled = true;
            defending = false;
            messages.add("You escaped combat, but the monster still blocks the room exit.");
        } else {
            messages.add("You failed to escape.");
            messages.addAll(monsterTurn());
        }
        return messages;
    }

    public ArrayList<String> monsterTurn() {
        ArrayList<String> messages = new ArrayList<>();
        if (!active || enemy == null || player == null || enemy.getHealth() <= 0) return messages;

        Attack attack = enemy.spinMonsterAttack();
        String flavor = attack.getFlavorText();
        if (flavor != null && !flavor.isEmpty()) {
            messages.add(flavor);
        } else {
            messages.add(enemy.getMonsterName() + " uses " + attack.getAttackName() + ".");
        }

        String effect = attack.getStatusEffect() == null ? "none" : attack.getStatusEffect().trim().toLowerCase();
        if (effect.equals("heal")) {
            int healed = attack.heal(enemy);
            messages.add(enemy.getMonsterName() + " heals " + healed + " HP.");
        } else if (effect.equals("defense")) {
            int boosted = attack.addDefense(enemy);
            messages.add(enemy.getMonsterName() + " raises defense by " + boosted + ".");
        } else {
            int damage = attack.calculateDamage(enemy, player);
            if (defending) damage /= 2;
            player.setHealth(Math.max(0, player.getHealth() - damage));
            messages.add(enemy.getMonsterName() + " deals " + damage + " damage.");
            applyStatusEffectIfRolled(attack, player, messages);
        }

        defending = false;
        if (player.getHealth() <= 0) {
            active = false;
            messages.add("You have died. The plague spreads...");
        }
        return messages;
    }

    private void applyStatusEffectIfRolled(Attack attack, Entity target, ArrayList<String> messages) {
        String effect = attack.getStatusEffect() == null ? "none" : attack.getStatusEffect().trim().toLowerCase();
        if (effect.equals("none") || effect.equals("heal") || effect.equals("defense")) return;

        double chance = attack.getStatusChance();
        if (chance <= 0) return;
        double roll = Math.random();
        if (roll > chance) return;

        if (effect.contains("poison")) {
            attack.poison(target);
            messages.add("Status effect applied: poison.");
        } else if (effect.contains("paraly")) {
            attack.paralyze(target);
            messages.add("Status effect applied: paralysis.");
        } else {
            messages.add("Status effect applied: " + attack.getStatusEffect() + ".");
        }
    }

    private ArrayList<String> defeatEnemy() {
        ArrayList<String> messages = new ArrayList<>();
        active = false;
        defending = false;
        messages.add("Defeated: " + enemy.getMonsterName());

        if (room != null) {
            removeEnemyFromRoom();
            messages.addAll(dropEnemyItemsToRoom());
        }
        return messages;
    }

    private void removeEnemyFromRoom() {
        if (room == null || enemy == null) return;
        String removeKey = null;
        for (String key : room.getMonsters().keySet()) {
            if (room.getMonsters().get(key) == enemy) {
                removeKey = key;
                break;
            }
        }
        if (removeKey != null) room.getMonsters().remove(removeKey);
    }

    private ArrayList<String> dropEnemyItemsToRoom() {
        ArrayList<String> messages = new ArrayList<>();
        if (enemy.getMonsterInventory() == null || enemy.getMonsterInventory().isEmpty()) {
            messages.add("No item drops.");
            return messages;
        }

        messages.add("Drops added to the room:");
        for (Items item : enemy.getMonsterInventory()) {
            String code = item.getItemName().toUpperCase().replaceAll("\\s+", "_");
            room.getItems().put(code, item);
            item.setLocation(room);
            messages.add("+ " + item.getItemName());
        }
        enemy.getMonsterInventory().clear();
        return messages;
    }

    private boolean canAct(ArrayList<String> messages) {
        if (!active || player == null || enemy == null) {
            messages.add("No active combat.");
            return false;
        }
        if (player.getHealth() <= 0) {
            active = false;
            messages.add("You are unable to fight.");
            return false;
        }
        if (enemy.getHealth() <= 0) {
            messages.addAll(defeatEnemy());
            return false;
        }
        return true;
    }

    public boolean isActive() {
        return active;
    }

    public boolean hasFled() {
        return fled;
    }

    public Player getPlayer() {
        return player;
    }

    public Monsters getEnemy() {
        return enemy;
    }

    public Room getRoom() {
        return room;
    }
}
