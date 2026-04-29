package TeamHydraSpring2026FX;

import java.io.*;
import java.util.*;

/**
 * Handles simple save/load files for the JavaFX and console versions.
 * The controller asks this class to persist game state; the controller does not
 * need to know the file format.
 */
public class GameSaveManager {

    public static String save(GameWorld world, Player player, Room currentRoom, String savePath) {
        if (world == null || player == null || currentRoom == null) {
            return "Save failed: game is not fully initialized.";
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(savePath))) {
            writer.println("ROOM~" + currentRoom.getRoomID());
            writer.println("PLAYER~" + player.getHealth() + "~" + player.getDamage() + "~" +
                    player.getDefense() + "~" + player.getSpeed() + "~" + player.getInventorySpace() + "~" + safe(player.getWeapon()));
            writer.println("INVENTORY~" + joinInventory(world, player));
            writer.println("EQUIPPED_WEAPON~" + equippedWeaponCode(world, player));
            writer.println("SOLVED_PUZZLES~" + joinSolvedPuzzles(world));
            writer.println("ROOM_ITEMS~" + joinRoomItems(world));
            writer.println("ROOM_MONSTERS~" + joinRoomMonsters(world));
            return "Game saved to " + savePath + ".";
        } catch (IOException e) {
            return "Save failed: " + e.getMessage();
        }
    }

    public static Room load(GameWorld world, Player player, String savePath) throws IOException {
        File file = new File(savePath);
        if (!file.exists()) {
            throw new FileNotFoundException("No save file found: " + savePath);
        }
        if (world == null || player == null) {
            throw new IOException("World/player not initialized for loading.");
        }

        Map<String, String> data = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                int idx = line.indexOf('~');
                if (idx > 0) data.put(line.substring(0, idx), line.substring(idx + 1));
            }
        }

        restorePlayer(player, data.get("PLAYER"));
        clearWorldCollections(world);
        restoreRoomItems(world, data.get("ROOM_ITEMS"));
        restoreRoomMonsters(world, data.get("ROOM_MONSTERS"));
        restoreSolvedPuzzles(world, data.get("SOLVED_PUZZLES"));
        restoreInventory(world, player, data.get("INVENTORY"));
        restoreEquippedWeapon(world, player, data.get("EQUIPPED_WEAPON"));

        String roomID = data.getOrDefault("ROOM", "R02").trim().toUpperCase();
        Room room = world.getRoom(roomID);
        if (room == null) room = world.getRoom("R02");
        if (room == null) room = world.getRoom("R01");
        if (room == null) throw new IOException("Save references missing room and no fallback room exists.");
        room.setVisited(true);
        player.setLocation(room);
        return room;
    }

    private static String equippedWeaponCode(GameWorld world, Player player) {
        if (player.getEquippedWeapon() == null) return "";
        String code = world.getItemCode(player.getEquippedWeapon());
        return code == null ? "" : code;
    }

    private static String joinInventory(GameWorld world, Player player) {
        ArrayList<String> codes = new ArrayList<>();
        for (Items item : player.getInventory()) {
            String code = world.getItemCode(item);
            if (code != null && !code.isEmpty()) codes.add(code);
        }
        return String.join(",", codes);
    }

    private static String joinSolvedPuzzles(GameWorld world) {
        ArrayList<String> codes = new ArrayList<>();
        for (Map.Entry<String, Puzzle> entry : world.getPuzzleCatalog().entrySet()) {
            if (entry.getValue().isSolved()) codes.add(entry.getKey());
        }
        return String.join(",", codes);
    }

    private static String joinRoomItems(GameWorld world) {
        ArrayList<String> groups = new ArrayList<>();
        for (Map.Entry<String, Room> roomEntry : world.getGameMap().getRooms().entrySet()) {
            if (!roomEntry.getValue().getItems().isEmpty()) {
                ArrayList<String> codes = new ArrayList<>();
                for (Map.Entry<String, Items> itemEntry : roomEntry.getValue().getItems().entrySet()) {
                    String code = world.getItemCode(itemEntry.getValue());
                    codes.add(code == null || code.isEmpty() ? itemEntry.getKey() : code);
                }
                groups.add(roomEntry.getKey() + ":" + String.join(",", codes));
            }
        }
        return String.join(";", groups);
    }

    private static String joinRoomMonsters(GameWorld world) {
        ArrayList<String> groups = new ArrayList<>();
        for (Map.Entry<String, Room> roomEntry : world.getGameMap().getRooms().entrySet()) {
            if (!roomEntry.getValue().getMonsters().isEmpty()) {
                groups.add(roomEntry.getKey() + ":" + String.join(",", roomEntry.getValue().getMonsters().keySet()));
            }
        }
        return String.join(";", groups);
    }

    private static void restorePlayer(Player player, String line) {
        if (line == null || line.isEmpty()) return;
        String[] p = line.split("~", -1);
        if (p.length > 0) player.setHealth(parseInt(p[0], 100));
        if (p.length > 1) player.setDamage(parseInt(p[1], 10));
        if (p.length > 2) player.setDefense(parseInt(p[2], 5));
        if (p.length > 3) player.setSpeed(parseInt(p[3], 5));
        if (p.length > 4) player.setInventorySpace(parseInt(p[4], 10));
    }

    private static void clearWorldCollections(GameWorld world) {
        for (Room room : world.getGameMap().getRooms().values()) {
            room.getItems().clear();
            room.getMonsters().clear();
        }
        for (Puzzle puzzle : world.getPuzzleCatalog().values()) {
            puzzle.setWinCondition(false);
        }
    }

    private static void restoreInventory(GameWorld world, Player player, String inventoryLine) {
        player.getInventory().clear();
        if (inventoryLine == null || inventoryLine.trim().isEmpty()) return;
        for (String code : inventoryLine.split(",")) {
            Items item = world.getCatalogItem(code.trim());
            if (item != null && player.getInventory().size() < player.getInventorySpace()) {
                player.getInventory().add(item);
            }
        }
    }

    private static void restoreEquippedWeapon(GameWorld world, Player player, String weaponCode) {
        if (weaponCode == null || weaponCode.trim().isEmpty()) return;
        Items item = world.getCatalogItem(weaponCode.trim().toUpperCase());
        if (item instanceof Weapon) {
            player.setEquippedWeaponDirect((Weapon) item);
        }
    }

    private static void restoreRoomItems(GameWorld world, String roomItemsLine) {
        if (roomItemsLine == null || roomItemsLine.trim().isEmpty()) return;
        for (String group : roomItemsLine.split(";")) {
            String[] parts = group.split(":", 2);
            if (parts.length < 2) continue;
            Room room = world.getRoom(parts[0].trim().toUpperCase());
            if (room == null) continue;
            for (String code : parts[1].split(",")) {
                code = code.trim().toUpperCase();
                Items item = world.getCatalogItem(code);
                if (item != null) {
                    room.getItems().put(code, item);
                    item.setLocation(room);
                }
            }
        }
    }

    private static void restoreRoomMonsters(GameWorld world, String roomMonstersLine) {
        if (roomMonstersLine == null || roomMonstersLine.trim().isEmpty()) return;
        for (String group : roomMonstersLine.split(";")) {
            String[] parts = group.split(":", 2);
            if (parts.length < 2) continue;
            Room room = world.getRoom(parts[0].trim().toUpperCase());
            if (room == null) continue;
            for (String code : parts[1].split(",")) {
                code = code.trim().toUpperCase();
                Monsters monster = world.getMonsterCatalog().get(code);
                if (monster != null) {
                    room.getMonsters().put(code, monster);
                    monster.setMonsterRoom(room);
                }
            }
        }
    }

    private static void restoreSolvedPuzzles(GameWorld world, String solvedLine) {
        if (solvedLine == null || solvedLine.trim().isEmpty()) return;
        for (String code : solvedLine.split(",")) {
            Puzzle puzzle = world.getPuzzleCatalog().get(code.trim().toUpperCase());
            if (puzzle != null) puzzle.setWinCondition(true);
        }
    }

    private static int parseInt(String text, int fallback) {
        try {
            return Integer.parseInt(text.trim());
        } catch (Exception e) {
            return fallback;
        }
    }

    private static String safe(String value) {
        return value == null ? "" : value.replace("~", " ");
    }
}
