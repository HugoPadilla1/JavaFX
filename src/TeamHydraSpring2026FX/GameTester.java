package TeamHydraSpring2026FX;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

/**
 * Console game harness for merged Team Hydra project.
 * This is intentionally controller-heavy so the next step can swap console output for JavaFX views.
 */
public class GameTester {
    private static final String SAVE_SLOT_1 = "hydra_console_save_slot_1.txt";
    private static final String SAVE_SLOT_2 = "hydra_console_save_slot_2.txt";

    private GameWorld world;
    private GameMap gameMap;
    private Room currentRoom;
    private Player player;
    private Scanner scanner;
    private boolean running;

    public GameTester(String roomsFilePath, String itemDataPath, String itemLocationDataPath,
                      String monstersPath, String attacksPath,
                      String puzzlesPath, String puzzleLocationPath,
                      String puzzleOutcomesPath, String puzzleRewardItemsPath) throws IOException {
        world = new GameWorld(roomsFilePath, itemDataPath, itemLocationDataPath,
                monstersPath, attacksPath, puzzlesPath, puzzleLocationPath, puzzleOutcomesPath, puzzleRewardItemsPath);
        gameMap = world.getGameMap();
        scanner = new Scanner(System.in);
        player = new Player();
        currentRoom = gameMap.getRoom("R02");

        if (currentRoom == null) {
            currentRoom = gameMap.getRoom("R01");
        }
        if (currentRoom == null) {
            throw new IOException("No valid starting room found. Expected R02 or R01.");
        }

        currentRoom.setVisited(true);
        player.setLocation(currentRoom);
    }

    public void start() {
        player.startGame(null);
        world.printWorldSummary();
        System.out.println("\n========== TEAM HYDRA CONSOLE GAME ==========");
        System.out.println("Type HELP for commands. This console loop is the test bed before JavaFX.\n");
        displayCurrentRoom();
        running = true;
        gameLoop();
    }

    private void gameLoop() {
        while (running && player.getHealth() > 0) {
            System.out.print("\n> ");
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) continue;

            String[] parts = input.split("\\s+", 2);
            String command = parts[0].toUpperCase();
            String argument = parts.length > 1 ? parts[1].trim() : "";

            switch (command) {
                case "N":
                case "E":
                case "S":
                case "W":
                case "U":
                case "D":
                    moveRoom(command);
                    break;
                case "MOVE":
                    moveCommand(argument);
                    break;
                case "LOOK":
                case "ROOM":
                    displayCurrentRoom();
                    break;
                case "EXPLORE":
                    explore();
                    break;
                case "ITEMS":
                    displayRoomItems();
                    break;
                case "MONSTERS":
                    displayRoomMonsters();
                    break;
                case "PUZZLES":
                    displayRoomPuzzles();
                    break;
                case "GRAB":
                case "TAKE":
                    grabItem(argument);
                    break;
                case "DROP":
                    dropItem(argument);
                    break;
                case "INV":
                case "INVENTORY":
                    displayInventory();
                    break;
                case "USE":
                    useItem(argument);
                    break;
                case "EQUIP":
                    equipItem(argument);
                    break;
                case "FIGHT":
                case "ATTACK":
                    fight(argument);
                    break;
                case "PUZZLE":
                case "SOLVE":
                    puzzle(argument);
                    break;
                case "STATUS":
                    player.viewStatus();
                    break;
                case "REST":
                    rest();
                    break;
                case "CATALOG":
                    displayCatalog();
                    break;
                case "GIVE":
                    giveItem(argument);
                    break;
                case "WORLD":
                    world.printWorldSummary();
                    break;
                case "SAVE":
                    saveGame(argument.equals("2") ? 2 : 1);
                    break;
                case "LOAD":
                    loadGame(argument.equals("2") ? 2 : 1);
                    break;
                case "TUTORIAL":
                    displayTutorial();
                    break;
                case "HELP":
                    displayHelp();
                    break;
                case "QUIT":
                case "EXIT":
                    quit();
                    break;
                default:
                    System.out.println("Invalid command. Type HELP for a command list.");
                    break;
            }
        }

        if (player.getHealth() <= 0) {
            System.out.println("\nYou have died. The plague spreads...");
        }
    }

    private void moveCommand(String argument) {
        if (argument.isEmpty()) {
            System.out.println("Usage: MOVE N/E/S/W/U/D");
            return;
        }
        moveRoom(argument.substring(0, 1).toUpperCase());
    }

    private void moveRoom(String direction) {
        direction = direction.toUpperCase();
        if (!currentRoom.hasExit(direction)) {
            System.out.println("You cannot go " + direction + " from here.");
            return;
        }

        if (!currentRoom.getMonsters().isEmpty()) {
            System.out.println("A monster blocks your escape. Defeat it or flee during combat first.");
            return;
        }

        String blockReason = world.getVerticalExitBlockReason(player, currentRoom, direction);
        if (!blockReason.isEmpty()) {
            System.out.println(blockReason);
            return;
        }

        String nextRoomID = currentRoom.getExit(direction);
        Room nextRoom = gameMap.getRoom(nextRoomID);
        if (nextRoom == null) {
            System.out.println("Error: Room " + nextRoomID + " not found.");
            return;
        }

        currentRoom = nextRoom;
        currentRoom.setVisited(true);
        player.setLocation(currentRoom);
        displayCurrentRoom();

        if (!currentRoom.getMonsters().isEmpty()) {
            System.out.println("\nDanger: You are not alone here. Type MONSTERS or FIGHT.");
        }
    }

    private void displayCurrentRoom() {
        currentRoom.displayRoomEntry();
        System.out.println("Room ID: " + currentRoom.getRoomID());
        System.out.println("Available exits: " + currentRoom.getExits().keySet());
        if (currentRoom.hasExit("U")) {
            String reason = world.getVerticalExitBlockReason(player, currentRoom, "U");
            System.out.println("Up stairs: " + (reason.isEmpty() ? "unlocked" : "locked - " + reason));
        }
        if (currentRoom.hasExit("D")) {
            String reason = world.getVerticalExitBlockReason(player, currentRoom, "D");
            System.out.println("Down stairs: " + (reason.isEmpty() ? "unlocked" : "locked - " + reason));
        }
        if (!currentRoom.getItems().isEmpty()) System.out.println("Items here: " + currentRoom.getItems().keySet());
        if (!currentRoom.getPuzzles().isEmpty())
            System.out.println("Puzzles here: " + currentRoom.getPuzzles().keySet());
        if (!currentRoom.getMonsters().isEmpty())
            System.out.println("Monsters here: " + currentRoom.getMonsters().keySet());
    }

    private void explore() {
        currentRoom.displayExplore(gameMap);
        displayRoomMonsters();
    }

    private void displayRoomItems() {
        System.out.println("-- Room Items --");
        if (currentRoom.getItems().isEmpty()) {
            System.out.println("No items in this room.");
            return;
        }
        for (Map.Entry<String, Items> entry : currentRoom.getItems().entrySet()) {
            System.out.println(entry.getKey() + " -> " + formatItem(entry.getValue()));
        }
    }

    private void displayRoomMonsters() {
        System.out.println("-- Monsters --");
        if (currentRoom.getMonsters().isEmpty()) {
            System.out.println("No monsters here.");
            return;
        }
        for (Map.Entry<String, Monsters> entry : currentRoom.getMonsters().entrySet()) {
            Monsters monster = entry.getValue();
            System.out.println(entry.getKey() + " -> " + monster);
            System.out.println("   " + monster.getMonsterDescription());
        }
    }

    private void displayRoomPuzzles() {
        System.out.println("-- Puzzles --");
        if (currentRoom.getPuzzles().isEmpty()) {
            System.out.println("No puzzles here.");
            return;
        }
        for (Map.Entry<String, Puzzle> entry : currentRoom.getPuzzles().entrySet()) {
            Puzzle puzzle = entry.getValue();
            System.out.println(entry.getKey() + " -> " + puzzle.getDescription() + (puzzle.isSolved() ? " [SOLVED]" : ""));
        }
    }

    private void grabItem(String itemCode) {
        if (itemCode.isEmpty()) {
            System.out.println("Usage: GRAB itemID");
            displayRoomItems();
            return;
        }

        itemCode = itemCode.toUpperCase();
        Items item = currentRoom.getItems().remove(itemCode);
        if (item == null) {
            System.out.println("That item is not in this room.");
            return;
        }
        if (player.getInventory().size() >= player.getInventorySpace()) {
            currentRoom.getItems().put(itemCode, item);
            System.out.println("Inventory full.");
            return;
        }

        player.getInventory().add(item);
        System.out.println("Picked up: " + itemCode + " -> " + item.getItemName());
    }

    private void dropItem(String itemCode) {
        Items item = findInventoryItem(itemCode);
        if (item == null) {
            System.out.println("You do not have that item.");
            return;
        }
        if (!item.getDroppable()) {
            System.out.println("You cannot drop that item.");
            return;
        }

        String code = world.getItemCode(item);
        if (code == null || code.isEmpty()) code = item.getItemName().toUpperCase();
        player.getInventory().remove(item);
        currentRoom.getItems().put(code, item);
        System.out.println("Dropped: " + code + " -> " + item.getItemName());
    }

    private void displayInventory() {
        System.out.println("-- Inventory " + player.getInventory().size() + "/" + player.getInventorySpace() + " --");
        if (player.getInventory().isEmpty()) {
            System.out.println("Inventory empty.");
            return;
        }
        for (Items item : player.getInventory()) {
            String code = world.getItemCode(item);
            System.out.println(code + " -> " + formatItem(item));
        }
    }

    private void useItem(String itemCode) {
        Items item = findInventoryItem(itemCode);
        if (item == null) {
            System.out.println("You do not have that item.");
            return;
        }

        if (item instanceof Consumable) {
            player.useItem(item, player.getInventory());
        } else if (item instanceof KeyItem) {
            KeyItem key = (KeyItem) item;
            key.itemEquipEffect(player);
            if (key.getKeyType() != null && key.getKeyType().equalsIgnoreCase("INV_CAP_20")) {
                player.setInventorySpace(20);
                System.out.println("Inventory capacity increased to 20.");
            }
        } else {
            System.out.println("That item is not usable. Try EQUIP if it is a weapon or wearable.");
        }
    }

    private void equipItem(String itemCode) {
        Items item = findInventoryItem(itemCode);
        if (item == null) {
            System.out.println("You do not have that item.");
            return;
        }

        if (item instanceof Weapon || item instanceof Wearable) {
            player.equipItem(item, player.getInventory());
        } else {
            System.out.println("That item cannot be equipped.");
        }
    }

    private void fight(String monsterCode) {
        Monsters enemy = findRoomMonster(monsterCode);
        if (enemy == null) {
            System.out.println("No matching monster here.");
            displayRoomMonsters();
            return;
        }

        Combat combat = world.createCombat();
        printMessages(combat.start(player, enemy, currentRoom));

        while (combat.isActive()) {
            System.out.println("\nYour HP: " + player.getHealth() + " | Enemy HP: " + enemy.getHealth());
            System.out.print("Combat [ATTACK / DEFEND / ITEM itemID / FLEE] > ");
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) continue;

            String[] parts = input.split("\\s+", 2);
            String command = parts[0].toUpperCase();
            String argument = parts.length > 1 ? parts[1].trim() : "";

            if (command.equals("ATTACK") || command.equals("A")) {
                printMessages(combat.playerAttack());
            } else if (command.equals("DEFEND") || command.equals("D")) {
                printMessages(combat.playerDefend());
            } else if (command.equals("ITEM") || command.equals("USE")) {
                Items item = findInventoryItem(argument);
                if (item instanceof Consumable) {
                    printMessages(combat.playerUseConsumable(item));
                } else {
                    System.out.println("Choose a consumable item.");
                }
            } else if (command.equals("FLEE") || command.equals("RETREAT")) {
                printMessages(combat.tryFlee());
            } else {
                System.out.println("Invalid combat command.");
            }
        }
    }

    private void puzzle(String puzzleCode) {
        Puzzle puzzle = findRoomPuzzle(puzzleCode);
        if (puzzle == null) {
            System.out.println("No matching puzzle here.");
            displayRoomPuzzles();
            return;
        }

        puzzle.setCurrentRoom(currentRoom);
        puzzle.setPlayer(player);
        puzzle.accessPuzzle();

        while (!puzzle.isSolved() && puzzle.getRemainingAttempts() > 0) {
            System.out.print("Puzzle [ANSWER text / HINT / EXIT] > ");
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("EXIT")) {
                puzzle.exitPuzzle();
                return;
            }
            if (input.equalsIgnoreCase("HINT")) {
                System.out.println("Hint: " + puzzle.giveHint());
                continue;
            }
            String answer = input;
            if (input.toUpperCase().startsWith("ANSWER ")) {
                answer = input.substring(7).trim();
            }
            printMessages(puzzle.submitAnswer(answer, player, currentRoom));
        }
    }

    private void printMessages(java.util.List<String> messages) {
        if (messages == null) return;
        for (String message : messages) {
            System.out.println(message);
        }
    }

    private void rest() {
        if (currentRoom.isSafeRoom()) {
            player.rest(currentRoom);
        } else {
            System.out.println("You can only fully rest in a safe room.");
        }
    }

    private void displayCatalog() {
        System.out.println("-- Loaded Item Catalog --");
        for (Map.Entry<String, Items> entry : world.getItemCatalog().entrySet()) {
            System.out.println(entry.getKey() + " -> " + formatItem(entry.getValue()));
        }
    }

    private void giveItem(String itemCode) {
        if (itemCode.isEmpty()) {
            System.out.println("Usage: GIVE itemID");
            return;
        }
        Items item = world.getCatalogItem(itemCode);
        if (item == null) {
            System.out.println("No item exists with that ID.");
            return;
        }
        player.getInventory().add(item);
        System.out.println("Debug added to inventory: " + itemCode.toUpperCase() + " -> " + item.getItemName());
    }

    private Items findInventoryItem(String itemCode) {
        if (itemCode == null || itemCode.trim().isEmpty()) {
            displayInventory();
            return null;
        }
        itemCode = itemCode.trim().toUpperCase();
        for (Items item : player.getInventory()) {
            String code = world.getItemCode(item);
            if (code != null && code.equalsIgnoreCase(itemCode)) return item;
            if (item.getItemName().equalsIgnoreCase(itemCode)) return item;
        }
        return null;
    }

    private Monsters findRoomMonster(String monsterCode) {
        if (currentRoom.getMonsters().isEmpty()) return null;
        if (monsterCode == null || monsterCode.trim().isEmpty()) {
            return currentRoom.getMonsters().values().iterator().next();
        }
        monsterCode = monsterCode.trim().toUpperCase();
        Monsters monster = currentRoom.getMonsters().get(monsterCode);
        if (monster != null) return monster;
        for (Monsters m : currentRoom.getMonsters().values()) {
            if (m.getMonsterName().equalsIgnoreCase(monsterCode)) return m;
        }
        return null;
    }

    private Puzzle findRoomPuzzle(String puzzleCode) {
        if (currentRoom.getPuzzles().isEmpty()) return null;
        if (puzzleCode == null || puzzleCode.trim().isEmpty()) {
            return currentRoom.getPuzzles().values().iterator().next();
        }
        String clean = puzzleCode.trim().toUpperCase();
        Puzzle puzzle = currentRoom.getPuzzles().get(clean);
        if (puzzle != null) return puzzle;
        String normalized = clean.startsWith("PUZ_") ? clean : String.format("PUZ_%02d", parseNumericID(clean));
        return currentRoom.getPuzzles().get(normalized);
    }

    private int parseNumericID(String text) {
        if (text == null) return 0;
        String digits = text.replaceAll("[^0-9]", "");
        return digits.isEmpty() ? 0 : Integer.parseInt(digits);
    }

    private String formatItem(Items item) {
        return item == null ? "null" : item.getItemName() + " | " + item.getClass().getSimpleName() + " | " + item.getItemDescription();
    }

    private void saveGame(int slot) {
        String path = slot == 1 ? SAVE_SLOT_1 : SAVE_SLOT_2;
        System.out.println(GameSaveManager.save(world, player, currentRoom, path));
    }

    private void loadGame(int slot) {
        String path = slot == 1 ? SAVE_SLOT_1 : SAVE_SLOT_2;
        try {
            world = new GameWorld("Rooms.txt", "itemData", "itemLocationData",
                    "Data/basedata/Monsters.txt", "Data/basedata/MonsterAttackData.txt",
                    "puzzles.txt", "puzzleLocationData", "puzzles_outcomes.txt", "puzzleRewardItems");
            gameMap = world.getGameMap();
            player = new Player();
            currentRoom = GameSaveManager.load(world, player, path);
            System.out.println("Loaded save slot " + slot + ".");
            displayCurrentRoom();
        } catch (Exception e) {
            System.out.println("Load failed: " + e.getMessage());
        }
    }

    private void displayTutorial() {
        System.out.println("\n========== TUTORIAL ==========");
        System.out.println("Objective: Escape the infected hospital by exploring rooms, collecting supplies, solving puzzles, and surviving combat.");
        System.out.println("Movement: Use N/E/S/W for normal movement. In lobby/stair rooms, U/D move between floors.");
        System.out.println("Items: GRAB itemID, USE itemID for consumables/key items, and EQUIP itemID for weapons/wearables.");
        System.out.println("Puzzles: PUZZLE puzzleID opens puzzle mode; solved puzzles can add rewards.");
        System.out.println("Combat: FIGHT monsterID starts combat; ATTACK/DEFEND/FLEE handle combat turns.");
        System.out.println("Saving: SAVE 1, SAVE 2, LOAD 1, or LOAD 2.");
    }

    private void displayHelp() {
        System.out.println("\n========== COMMANDS ==========");
        System.out.println("N/E/S/W or MOVE N/E/S/W  - Move between rooms");
        System.out.println("LOOK or ROOM             - Redisplay current room");
        System.out.println("EXPLORE                  - Show exits, items, puzzles, and monsters");
        System.out.println("ITEMS                    - Show room items");
        System.out.println("MONSTERS                 - Show monsters in current room");
        System.out.println("PUZZLES                  - Show puzzles in current room");
        System.out.println("GRAB itemID              - Pick up item from room");
        System.out.println("DROP itemID              - Drop item into room");
        System.out.println("INVENTORY or INV         - Show held items");
        System.out.println("USE itemID               - Use consumable/key item");
        System.out.println("EQUIP itemID             - Equip weapon/wearable");
        System.out.println("FIGHT [monsterID]        - Fight monster in room");
        System.out.println("PUZZLE [puzzleID]        - Enter puzzle mode");
        System.out.println("STATUS                   - Show player stats");
        System.out.println("REST                     - Rest in safe room");
        System.out.println("CATALOG                  - Show every loaded item");
        System.out.println("GIVE itemID              - Debug-add item to inventory");
        System.out.println("WORLD                    - Print world summary");
        System.out.println("QUIT                     - Exit tester");
        System.out.println("==============================\n");
    }

    private void quit() {
        player.quitGame();
        System.out.println("Thanks for testing!");
        running = false;
    }

    public static void main(String[] args) {
        try {
            String roomsPath = args.length > 0 ? args[0] : "Rooms.txt";
            String itemDataPath = args.length > 1 ? args[1] : "itemData";
            String itemLocationPath = args.length > 2 ? args[2] : "itemLocationData";
            String monstersPath = args.length > 3 ? args[3] : "Data/basedata/Monsters.txt";
            String attacksPath = args.length > 4 ? args[4] : "Data/basedata/MonsterAttackData.txt";
            String puzzlesPath = args.length > 5 ? args[5] : "puzzles.txt";
            String puzzleLocationPath = args.length > 6 ? args[6] : "puzzleLocationData";
            String puzzleOutcomesPath = args.length > 7 ? args[7] : "puzzles_outcomes.txt";
            String puzzleRewardItemsPath = args.length > 8 ? args[8] : "puzzleRewardItems";

            GameTester tester = new GameTester(roomsPath, itemDataPath, itemLocationPath,
                    monstersPath, attacksPath, puzzlesPath, puzzleLocationPath, puzzleOutcomesPath, puzzleRewardItemsPath);
            tester.start();
        } catch (IOException e) {
            System.err.println("Error loading game world: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
