package TeamHydraSpring2026FX;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Central data-driven world controller.
 * Loads rooms, items, monsters, attacks, puzzle definitions, puzzle placement, and puzzle rewards.
 */
public class GameWorld {
    private GameMap gameMap;
    private final HashMap<String, Items> itemCatalog = new HashMap<>();
    private final HashMap<Items, String> itemCodes = new HashMap<>();
    private final HashMap<String, ArrayList<Items>> monsterDrops = new HashMap<>();
    private final HashMap<String, ArrayList<Items>> puzzleRewards = new HashMap<>();
    private final HashMap<String, Monsters> monsterCatalog = new HashMap<>();
    private final HashMap<Integer, Attack> attackCatalog = new HashMap<>();
    private final HashMap<String, Puzzle> puzzleCatalog = new HashMap<>();

    public GameWorld(String roomsFilePath, String itemDataPath, String itemLocationDataPath) throws IOException {
        this(roomsFilePath, itemDataPath, itemLocationDataPath,
                "Data/basedata/Monsters.txt", "Data/basedata/MonsterAttackData.txt",
                "puzzles.txt", "puzzleLocationData", "puzzles_outcomes.txt", "puzzleRewardItems");
    }

    public GameWorld(String roomsFilePath, String itemDataPath, String itemLocationDataPath,
                     String monstersPath, String monsterAttackPath,
                     String puzzlesPath, String puzzleLocationPath,
                     String puzzleOutcomesPath, String puzzleRewardItemsPath) throws IOException {
        loadRooms(roomsFilePath);
        loadItems(itemDataPath);
        loadAttacks(monsterAttackPath);
        loadMonsters(monstersPath);
        loadItemLocations(itemLocationDataPath);
        loadPuzzles(puzzlesPath);
        loadPuzzleOutcomes(puzzleOutcomesPath);
        loadPuzzleRewardItems(puzzleRewardItemsPath);
        loadPuzzleLocations(puzzleLocationPath);
        placeMonstersUsingFallbackMap();
    }

    private void loadRooms(String roomsFilePath) throws IOException {
        gameMap = new GameMap(roomsFilePath);
    }

    public void loadItems(String itemDataPath) throws IOException {
        File file = new File(itemDataPath);
        if (!file.exists()) {
            System.out.println("No item data file found: " + itemDataPath);
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (shouldSkip(line) || line.equalsIgnoreCase("ID~Name~Category~Description~HP~DMG~DEF~SPD~STATUS~DURATION~SPECIAL"))
                    continue;

                String[] data = line.split("~", -1);
                if (data.length < 11) {
                    System.out.println("Skipping malformed item line: " + line);
                    continue;
                }

                String itemCode = data[0].trim().toUpperCase();
                Items item = createItem(itemCode, data[1].trim(), data[2].trim(), data[3].trim(),
                        parseIntSafe(data[4]), parseIntSafe(data[5]), parseIntSafe(data[6]), parseIntSafe(data[7]),
                        data[8].trim(), parseIntSafe(data[9]), data[10].trim());
                if (item != null) {
                    itemCatalog.put(itemCode, item);
                    itemCodes.put(item, itemCode);
                }
            }
        }
    }

    private Items createItem(String itemCode, String name, String category, String description,
                             int hp, int damage, int defense, int speed,
                             String status, int duration, String special) {
        int numericID = parseNumericID(itemCode);
        String cleanCategory = category.toLowerCase().replace(" ", "");
        Room noLocationYet = null;

        switch (cleanCategory) {
            case "consumable":
                return new Consumable(numericID, name, description, true, 100, noLocationYet,
                        hp, 0, speed, 0, defense, 0, status, duration);
            case "weapon":
                return new Weapon(numericID, name, description, true, 100, noLocationYet, damage, hp);
            case "wearable":
            case "armor":
            case "accessory":
                return new Wearable(numericID, name, description, true, 100, noLocationYet,
                        category, hp, hp, defense, defense, speed, speed);
            case "keyitem":
                return new KeyItem(numericID, name, description, false, 0, noLocationYet,
                        special, parseNumericID(status), false, true);
            default:
                System.out.println("Unknown item category for " + itemCode + ": " + category);
                return null;
        }
    }

    public void loadItemLocations(String itemLocationDataPath) throws IOException {
        File file = new File(itemLocationDataPath);
        if (!file.exists()) {
            System.out.println("No item location file found: " + itemLocationDataPath);
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (shouldSkip(line) || line.equalsIgnoreCase("ItemID~Room~Monster~Puzzle")) continue;

                String[] data = line.split("~", -1);
                if (data.length < 4) {
                    System.out.println("Skipping malformed item location line: " + line);
                    continue;
                }

                String itemCode = data[0].trim().toUpperCase();
                Items item = itemCatalog.get(itemCode);
                if (item == null) {
                    System.out.println("Item location references missing item: " + itemCode);
                    continue;
                }

                assignItemToRooms(itemCode, item, data[1]);
                assignItemToMonsterDrops(item, data[2]);
                assignItemToPuzzleRewards(item, data[3]);
            }
        }
    }

    public void loadAttacks(String attackPath) throws IOException {
        File file = new File(attackPath);
        if (!file.exists()) {
            System.out.println("No monster attack file found: " + attackPath);
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (shouldSkip(line) || line.toLowerCase().startsWith("attackid~")) continue;

                String[] data = line.split("~", -1);
                if (data.length < 5) {
                    System.out.println("Skipping malformed attack line: " + line);
                    continue;
                }

                int id = parseIntSafe(data[0]);
                String flavor = data.length > 5 ? data[5].trim() : "";
                attackCatalog.put(id, new Attack(id, data[1].trim(), parseDoubleSafe(data[2]), data[3].trim(), parseDoubleSafe(data[4]), flavor));
            }
        }
    }

    public void loadMonsters(String monstersPath) throws IOException {
        File file = new File(monstersPath);
        if (!file.exists()) {
            System.out.println("No monster file found: " + monstersPath);
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (shouldSkip(line) || line.toLowerCase().startsWith("monsterid")) continue;

                String[] data = line.split("~", -1);
                if (data.length < 9) {
                    System.out.println("Skipping malformed monster line: " + line);
                    continue;
                }

                int id = parseIntSafe(data[0]);
                Monsters monster = new Monsters(id, data[1].trim(), data[2].trim(), data[3].trim(),
                        Boolean.parseBoolean(data[4].trim()), parseIntSafe(data[5]), parseIntSafe(data[6]),
                        parseIntSafe(data[7]), parseIntSafe(data[8]));
                monster.setMonsterAttackAL(buildDefaultAttackListForMonster(id));
                monsterCatalog.put(monsterCode(id), monster);
            }
        }
    }

    private ArrayList<Attack> buildDefaultAttackListForMonster(int monsterID) {
        ArrayList<Attack> list = new ArrayList<>();
        int[][] mapping;
        switch (monsterID) {
            case 1:
                mapping = new int[][]{{1, 4}, {2, 2}, {0, 4}};
                break;
            case 2:
                mapping = new int[][]{{3, 3}, {4, 5}, {0, 2}};
                break;
            case 3:
                mapping = new int[][]{{5, 4}, {6, 5}, {0, 1}};
                break;
            case 4:
                mapping = new int[][]{{7, 4}, {8, 4}, {0, 2}};
                break;
            case 5:
                mapping = new int[][]{{9, 5}, {10, 3}, {0, 2}};
                break;
            case 6:
                mapping = new int[][]{{11, 4}, {12, 3}, {13, 2}, {0, 1}};
                break;
            case 7:
                mapping = new int[][]{{14, 5}, {15, 1}, {16, 3}, {0, 1}};
                break;
            case 8:
                mapping = new int[][]{{17, 4}, {18, 2}, {19, 3}, {20, 1}};
                break;
            default:
                mapping = new int[][]{{0, 10}};
                break;
        }
        for (int[] pair : mapping) {
            Attack attack = attackCatalog.get(pair[0]);
            if (attack != null) {
                for (int i = 0; i < pair[1]; i++) list.add(attack);
            }
        }
        if (list.isEmpty()) list.add(new Attack(0, "Basic Attack", 1.0, "none", 0, "The monster attacks."));
        return list;
    }

    public void loadPuzzles(String puzzlesPath) throws IOException {
        File file = new File(puzzlesPath);
        if (!file.exists()) {
            System.out.println("No puzzles file found: " + puzzlesPath);
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (shouldSkip(line)) continue;

                String[] data = line.split(",", -1);
                if (data.length < 8) {
                    System.out.println("Skipping malformed puzzle line: " + line);
                    continue;
                }

                String code = data[0].trim().toUpperCase();
                Puzzle puzzle = new Puzzle(data[4].trim(), data[2].trim(), parseIntSafe(data[7]));
                puzzle.setPuzzleID(parseNumericID(code));
                puzzle.setWinMessage(data[5].trim());
                puzzle.setHintMessage(data[6].trim());
                puzzle.setFailMessage("Incorrect answer.");
                puzzle.setSuccessResult("Puzzle solved.");
                puzzleCatalog.put(code, puzzle);
            }
        }
    }

    public void loadPuzzleLocations(String puzzleLocationPath) throws IOException {
        File file = new File(puzzleLocationPath);
        if (!file.exists()) {
            System.out.println("No puzzle location file found: " + puzzleLocationPath);
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (shouldSkip(line) || line.equalsIgnoreCase("PuzzleID~Room")) continue;

                String[] data = line.split("~", -1);
                if (data.length < 2) continue;

                String puzzleCode = normalizePuzzleCode(data[0]);
                Puzzle puzzle = puzzleCatalog.get(puzzleCode);
                if (puzzle == null) {
                    System.out.println("Puzzle location references missing puzzle: " + puzzleCode);
                    continue;
                }

                for (String roomID : data[1].split(",")) {
                    Room room = gameMap.getRoom(roomID.trim().toUpperCase());
                    if (room != null) room.getPuzzles().put(puzzleCode, puzzle);
                }
            }
        }
    }

    public void loadPuzzleOutcomes(String puzzleOutcomesPath) throws IOException {
        File file = new File(puzzleOutcomesPath);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (shouldSkip(line)) continue;

                String[] data = line.split(",", -1);
                if (data.length < 5) continue;

                Puzzle puzzle = puzzleCatalog.get(normalizePuzzleCode(data[0]));
                if (puzzle != null) {
                    puzzle.setSuccessResult(data[1].trim());
                    puzzle.setFailMessage(data[2].trim());
                    puzzle.setPenalty(data[3].trim());
                    puzzle.setReward(data[4].trim());
                }
            }
        }
    }

    public void loadPuzzleRewardItems(String puzzleRewardItemsPath) throws IOException {
        File file = new File(puzzleRewardItemsPath);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (shouldSkip(line) || line.equalsIgnoreCase("PuzzleID~RewardItems")) continue;

                String[] data = line.split("~", -1);
                if (data.length < 2) continue;

                String puzzleCode = normalizePuzzleCode(data[0]);
                Puzzle puzzle = puzzleCatalog.get(puzzleCode);
                if (puzzle == null || isNone(data[1])) continue;

                for (String itemCode : data[1].split(",")) {
                    Items item = itemCatalog.get(itemCode.trim().toUpperCase());
                    if (item != null) puzzle.getRewardItems().add(item);
                }
            }
        }
    }

    private void assignItemToRooms(String itemCode, Items item, String roomField) {
        if (isNone(roomField)) return;
        for (String roomID : roomField.split(",")) {
            roomID = roomID.trim().toUpperCase();
            if (isNone(roomID)) continue;
            Room room = gameMap.getRoom(roomID);
            if (room != null) {
                room.getItems().put(itemCode, item);
                item.setLocation(room);
            } else {
                System.out.println("Could not place " + itemCode + ". Missing room: " + roomID);
            }
        }
    }

    private void assignItemToMonsterDrops(Items item, String monsterField) {
        if (isNone(monsterField)) return;
        for (String monsterID : monsterField.split(",")) {
            monsterID = monsterID.trim().toUpperCase();
            if (isNone(monsterID)) continue;
            monsterDrops.putIfAbsent(monsterID, new ArrayList<>());
            monsterDrops.get(monsterID).add(item);
            Monsters monster = monsterCatalog.get(monsterID);
            if (monster != null) monster.getMonsterInventory().add(item);
        }
    }

    private void assignItemToPuzzleRewards(Items item, String puzzleField) {
        if (isNone(puzzleField)) return;
        for (String puzzleID : puzzleField.split(",")) {
            String puzzleCode = normalizePuzzleCode(puzzleID);
            puzzleRewards.putIfAbsent(puzzleCode, new ArrayList<>());
            puzzleRewards.get(puzzleCode).add(item);
        }
    }

    private void placeMonstersUsingFallbackMap() {
        String[][] placement = {
                {"M01", "R04"}, {"M02", "R06"}, {"M03", "R14"}, {"M04", "R16"},
                {"M05", "R03"}, {"M06", "R19"}, {"M07", "R12"}, {"M08", "R21"},
                {"B01", "R19"}, {"B02", "R21"}
        };

        for (String[] pair : placement) {
            Monsters monster = monsterCatalog.get(pair[0]);
            Room room = gameMap.getRoom(pair[1]);
            if (monster != null && room != null && !room.getMonsters().containsKey(pair[0])) {
                room.getMonsters().put(pair[0], monster);
                monster.setMonsterRoom(room);
            }
        }
    }

    private String monsterCode(int id) {
        if (id >= 6) return String.format("B%02d", id - 5);
        return String.format("M%02d", id);
    }

    private String normalizePuzzleCode(String text) {
        return String.format("PUZ_%02d", parseNumericID(text));
    }

    private boolean shouldSkip(String line) {
        return line == null || line.trim().isEmpty() || line.trim().startsWith("#");
    }

    private boolean isNone(String value) {
        return value == null || value.trim().isEmpty() || value.trim().equalsIgnoreCase("NONE") || value.trim().equalsIgnoreCase("N/A");
    }

    private int parseIntSafe(String text) {
        try {
            return Integer.parseInt(text.trim());
        } catch (Exception e) {
            return 0;
        }
    }

    private double parseDoubleSafe(String text) {
        try {
            return Double.parseDouble(text.trim());
        } catch (Exception e) {
            return 0.0;
        }
    }

    private int parseNumericID(String text) {
        if (text == null) return 0;
        String digits = text.replaceAll("[^0-9]", "");
        return digits.isEmpty() ? 0 : Integer.parseInt(digits);
    }

    public Combat createCombat() {
        return new Combat();
    }

    public GameMap getGameMap() {
        return gameMap;
    }

    public Room getRoom(String roomID) {
        return gameMap.getRoom(roomID);
    }

    public Items getCatalogItem(String itemCode) {
        return itemCode == null ? null : itemCatalog.get(itemCode.trim().toUpperCase());
    }

    public String getItemCode(Items item) {
        return itemCodes.get(item);
    }

    public HashMap<String, Items> getItemCatalog() {
        return itemCatalog;
    }

    public HashMap<String, Monsters> getMonsterCatalog() {
        return monsterCatalog;
    }

    public HashMap<String, Puzzle> getPuzzleCatalog() {
        return puzzleCatalog;
    }

    public HashMap<String, ArrayList<Items>> getMonsterDrops() {
        return monsterDrops;
    }

    public HashMap<String, ArrayList<Items>> getPuzzleRewards() {
        return puzzleRewards;
    }

    public HashMap<Integer, Attack> getAttackCatalog() {
        return attackCatalog;
    }

    public void printWorldSummary() {
        System.out.println("\n========== WORLD SUMMARY ==========");
        System.out.println("Items loaded: " + itemCatalog.size());
        System.out.println("Attacks loaded: " + attackCatalog.size());
        System.out.println("Monsters loaded: " + monsterCatalog.size());
        System.out.println("Puzzles loaded: " + puzzleCatalog.size());
        System.out.println("Monster drop groups: " + monsterDrops.size());
        System.out.println("Puzzle reward groups: " + puzzleRewards.size());
        System.out.println("===================================\n");
    }
}
