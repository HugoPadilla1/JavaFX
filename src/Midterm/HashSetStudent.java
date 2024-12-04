package Midterm;

import java.util.HashSet;
import java.util.Iterator;

public class HashSetStudent
{
    private HashSet<String> mySet = new HashSet<String>();
    
    
    public HashSetStudent(String[] initialValues)
    {
        for (int i=0; i< initialValues.length; i++)
        {
            mySet.add(initialValues[i]);
        }
    }

    public void add(String data)
    {
        mySet.add(data);
    }
    
    public void remove(String data)
    {
        mySet.remove(data);
    }

    public static void main(String[] args)
    {
       // create instance
        String initialSet[] = {"John", "Sue", "Romeo", "Juliet", "Adam", "Sue", "Homer", "Marge", "Bart", "Ellen"};
        HashSetStudent aSet = new HashSetStudent(initialSet);

        // YOUR CODE GOES HERE
        // using the instance of Midterm.HashSetStudent named aSet, add code that
        // 1. Creates an iterator
        Iterator<String> iterator = aSet.mySet.iterator();

        // 2. Uses the iterator to print the contents of the hashset to console.
        System.out.println("Student names in the set:");
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
        iterator = aSet.mySet.iterator();
        // DO NOT JUST PRINT THE SET - Iterate through the set and print individual names each on their own line
        //DO NOT USE AN ENHANCED FOR. MUST USE THE ITERATOR
        // 3.  Find the name in the hashSet that is the shortest and print it to console (Note:  Don't just
        // print "Sue" .  Do it by checking each element in the HashSetUser hashset. You must use an iterator.)
        if (iterator.hasNext()) {
            String shortestName = iterator.next();  // Initialize with the first name
            while (iterator.hasNext()) {
                String currentName = iterator.next();
                if (currentName.length() < shortestName.length()) {
                    shortestName = currentName;
                }
            }

            // Print the shortest name
            System.out.println("\nThe shortest name is: " + shortestName);
        } else {
            System.out.println("\nThe set is empty.");
        }
    }
}
