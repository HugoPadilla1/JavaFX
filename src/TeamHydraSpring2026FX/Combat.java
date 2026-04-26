package TeamHydraSpring2026FX;

import java.util.ArrayList;
import java.util.Scanner;

/*
Combat class coders: Samuel Michel
The purpose of this class is to handle the different attack chances.

Create an ArrayList<Attack> that holds 10 Attack objects for each monster.
If a monster's attack will have a 40% chance to roll, then add it to the list 4 times.
Use integer.randomInt() to pick a number between -1 and 10 (not inclusive)
Based on the roll, choose that attack to play against the other entity.


1. Load player and monster
2. Speed determines who goes first
3. Basic attributes: health, damage, defense

TODO:
-Insert the game lost command.

 */
public class Combat {
    public void initiateCombat(Player user, Monsters enemy, boolean inCombat) {
        //check fastest enemy
        Scanner combatSC = new Scanner(System.in);
        String playerMove;
        int causeDamage = 0;
        Attack monsterMove;

        //The following assumes that the player speed is > the monster speed
        while (inCombat) {
            if (user.getHealth() <= 0) {
                inCombat = false;
                System.out.println("You have died. The plague spreads...");
                //TODO: Game.lost command GOES HERE
            } else if (enemy.getHealth() <= 0) {
                inCombat = false;
                break;
            } else { //combat
                boolean isDefending = false;
                boolean playerDefendedLastTurn = false;
                boolean validCombatCommand = false;

                //this loop allows the player to cancel using an item, or if they make a typo they can type the
                //corrected command.
                while (!validCombatCommand) {
                    System.out.println("What's your next move? Type attack, item, or retreat");
                    playerMove = combatSC.next();

                    //Player chooses attack
                    if (playerMove.equalsIgnoreCase("attack")) {
                    /* DAMAGE FORMULA
                    Damage = (Damage Multiplier) * Initiator Attack Stat + Equipped Weapon Attack Stat – Defender’s Defense Stat
                    */
                        causeDamage = user.attack() - enemy.getDefense();
                        if (causeDamage < 0) {      //if the player's damage is greater than the enemy's defense, subtract from enemy health
                            enemy.setHealth(enemy.getHealth() - causeDamage);
                        } else {
                            System.out.println("The enemy's defense is too high! 0 damage caused.");
                        }
                        validCombatCommand = true;
                    }//end Player chooses attack

                    //Method for when the player chooses to defend. This results in the monster's damage to the player to be
                    //reduced by 50%
                    if (playerMove.equalsIgnoreCase("defend")) {
                        if (playerDefendedLastTurn) {
                            System.out.println("You can't defend two turns in a row!");
                        } else {
                            isDefending = true;
                            playerDefendedLastTurn = true;
                            System.out.println("You brace yourself for the next attack!");
                            validCombatCommand = true;
                        }
                    } else {
                        playerDefendedLastTurn = false; //resets the variable
                    }//end defend usage

                    //If the player chooses to flee:
                    if (playerMove.equalsIgnoreCase("flee")) {
                        int fleeChance = (int) (Math.random() * 100) + user.getSpeed() - enemy.getSpeed();
                        if (fleeChance < 50) { // 50% chance to flee, adjust as needed
                            System.out.println("You successfully fled the battle!");
                            inCombat = false;
                            break;
                        } else {
                            System.out.println("You failed to escape!");
                            validCombatCommand = true;
                            // Monster still attacks
                        }
                    }//end flee usage

                    //If the player chooses to use an item:
                    if (playerMove.equalsIgnoreCase("item")) {
                        boolean usingItem = true;
                        while (usingItem) {
                            System.out.println("Which consumable would you like to use?");
                            String itemName = combatSC.next();
                            Items itemToUse = null;
                            if (itemName.equalsIgnoreCase("cancel")) {
                                usingItem = false;
                                break;
                            }
                            for (Items item : user.getInventory()) {
                                if (item.getItemName().equalsIgnoreCase(itemName) && item instanceof Consumable) {
                                    itemToUse = item;
                                    break;
                                }
                            }
                            if (itemToUse != null) {
                                user.useItem(itemToUse, user.getInventory());
                                System.out.println("You used " + itemName + "!");
                                validCombatCommand = true;
                            } else {
                                System.out.println("You don't have that consumable item!\nTry typing a different item, or type 'cancel'.");
                            }
                        }
                    }//end item usage

                    else System.out.println("Invalid input received, or you've canceled action.");
                }//end validCombatCommand loop; continue to the monster's attack if validCombatCommand is true.

                //Monster attack
                monsterMove = enemy.spinMonsterAttack();
                causeDamage = (int) (monsterMove.getDamageMultiplier() * enemy.getDamage()) - user.getDefense();
                //If the player defends, reduce the monster's damage to the user by 50%
                if (isDefending) {
                    causeDamage = causeDamage / 2;
                    user.setHealth(user.getHealth() - causeDamage);
                    isDefending = false; // Reset after use
                    System.out.println("You position yourself defensively...");
                } else {
                    if (causeDamage < 0) {      //if the monster's damage is greater than the user's defense, subtract from user health
                        user.setHealth(user.getHealth() - causeDamage);
                        System.out.println("The monster has struck! You have taken " + causeDamage + " damage! You health is now at " + user.getHealth() + "\nMonster health: " + enemy.getHealth());
                    } else {
                        System.out.println("The adventurer's defense is too high! 0 damage caused.");
                    }
                }
            }//end combat code block
        }//end combat loop
        combatSC.close();
    }
}
