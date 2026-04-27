package CourseFinal2024;

import java.io.*;
import java.util.*;

public class NameMap {
    private String[] names = {"Fred", "Tim", "Tina", "Sally", "John", "Latoria", "Bliss", "Josh", "Doug",
            "Calvin", "Dan", "Richard", "Andrew", "Jared", "David", "Helen", "Sam", "Laura", "Sarah", "Elise"};

    public static void main(String[] args) {
        // Create a Map to hold an integer and a name. You should store all of the names in the above array.
        NameMap nameMapFinal = new NameMap();
        Map<Integer, List<String>> nameMap = new TreeMap<>();
        Random random = new Random();
        // Please generate a random number for the integer. Limit your random number between 1 and 10.
        for (String name : nameMapFinal.names) {
            int randomKey = random.nextInt(10) + 1; // Limited 1-10.
            nameMap.computeIfAbsent(randomKey, k -> new ArrayList<>()).add(name);
        }
        // If you have a duplicate key, you should store both names.
        // Print the map using the custom method
        nameMapFinal.printMap(nameMap);

        // Write the map to a binary file
        String fileName = "nameMapData.bin";
        writeMapToFile(nameMap, fileName);

        // Read the map from the binary file and print it
        System.out.println("Contents of the map read from the file: " + fileName);
        Map<Integer, List<String>> readMap = readMapFromFile(fileName);
        nameMapFinal.printMap(readMap);
    }

    // Create a method that prints your map. This must use an iterator.
    public void printMap(Map<Integer, List<String>> map) {
        Iterator<Map.Entry<Integer, List<String>>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, List<String>> entry = iterator.next();
            System.out.println("Key: " + entry.getKey() + ", Values: " + entry.getValue());
        }
    }

    // Do not simply print a data structure. You must print each element in the data structure.
    // Method to write the map to a binary file
    public static void writeMapToFile(Map<Integer, List<String>> map, String fileName) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(map);
            System.out.println("File has been written: " + fileName);
        } catch (IOException e) {
            System.err.println("Error writing map to file: " + e.getMessage());
        }
    }

    // Method to read the map from a binary file
    @SuppressWarnings("unchecked")
    public static Map<Integer, List<String>> readMapFromFile(String fileName) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            return (Map<Integer, List<String>>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error reading map from file: " + e.getMessage());
        }
        return new TreeMap<>(); // Return an empty map in case of error
    }
}

