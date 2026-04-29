package TeamHydraSpring2026FX;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/**
 * Loads and parses Rooms.txt into a HashMap of Room objects.
 * Each room block is separated by "END". Blank lines and # comments are ignored.
 */
public class GameMap {
    private HashMap<String, Room> rooms;

    public GameMap(String filePath) throws IOException {
        rooms = new HashMap<>();
        loadRooms(filePath);
    }

    private void loadRooms(String filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            Room currentRoom = null;
            String pendingDesc = null;

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                // Skip blank lines and comments
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }

                if (line.startsWith("ROOM:")) {
                    // ROOM:<roomID>:<roomName>:<isSafeRoom>
                    String[] parts = line.split(":", 4);
                    if (parts.length < 4) {
                        System.err.println("Malformed ROOM line: " + line);
                        continue;
                    }
                    String roomID = parts[1].trim();
                    String roomName = parts[2].trim();
                    boolean isSafeRoom = Boolean.parseBoolean(parts[3].trim());

                    // Description not yet known; pass placeholder, overwrite after DESC line
                    currentRoom = new Room(roomID, roomName, "", isSafeRoom);
                    pendingDesc = null;

                } else if (line.startsWith("DESC:") && currentRoom != null) {
                    currentRoom.setRoomDescription(line.substring(5).trim());

                } else if (line.startsWith("DOOR:") && currentRoom != null) {
                    String[] parts = line.split(":", 3);
                    if (parts.length >= 3) {
                        currentRoom.addDoorDescription(parts[1].trim(), parts[2].trim());
                    }

                } else if (line.startsWith("EXIT:") && currentRoom != null) {
                    String[] parts = line.split(":", 3);
                    if (parts.length >= 3) {
                        currentRoom.addExit(parts[1].trim(), parts[2].trim());
                    }

                } else if (line.equals("END") && currentRoom != null) {
                    rooms.put(currentRoom.getRoomID(), currentRoom);
                    currentRoom = null;
                    pendingDesc = null;
                }
            }

            if (currentRoom != null) {
                System.err.println("Warning: room " + currentRoom.getRoomID()
                        + " was not terminated with END — adding anyway.");
                rooms.put(currentRoom.getRoomID(), currentRoom);
            }
        }

        System.out.println("GameMap loaded: " + rooms.size() + " rooms.");
    }

    public Room getRoom(String roomID) {
        return rooms.get(roomID);
    }

    public HashMap<String, Room> getRooms() {
        return rooms;
    }
}
