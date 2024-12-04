package Midterm;

import java.util.Random;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


public class HashMapPets
{
    
    public static void main(String[] args)
    {
       // create instance
        String initialSet[] = {"Sam", "Smokey", "Bella", "Mitten", "Dusty", "Kenai", "Zeke", "Ranger", "Finn", "Emma"};
       
        Random randomGenerator = new Random();

        // Create a HashMap with String as key and Integer as value
        HashMap<String, Integer> petMap = new HashMap<>();

        // Populate the HashMap
        for (String pet : initialSet) {
            // Generate a random integer between 100 and 200
            int randomValue = 100 + randomGenerator.nextInt(101); // (101 gives a range from 0 to 100 inclusive)
            petMap.put(pet, randomValue);
        }

        // Get the set of entries from the HashMap
        Set<Map.Entry<String, Integer>> entrySet = petMap.entrySet();

        // Create an iterator for the entry set
        Iterator<Map.Entry<String, Integer>> iterator = entrySet.iterator();

        // Print each key-value pair using the iterator
        while (iterator.hasNext()) {
            Map.Entry<String, Integer> entry = iterator.next();
            System.out.println("Pet Name: " + entry.getKey() + " - Random Value: " + entry.getValue());
        }
    }
}
