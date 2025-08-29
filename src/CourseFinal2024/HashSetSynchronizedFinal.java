package CourseFinal2024;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class HashSetSynchronizedFinal {
    private Set<Character> hashSet = new HashSet();
    private Thread thread1;
    private Thread thread2;

    class Task1 implements Runnable {
        public void run() {
            while (true) {
                synchronized (hashSet) {
                    if (hashSet.size() <= 1) {
                        break; // To stop when only one letter remains
                    }
                    Iterator<Character> iterator = hashSet.iterator();
                    if (iterator.hasNext()) {
                        Character letter = iterator.next();
                        iterator.remove();
                    }
                }
                try {
                    Thread.sleep(500); // 0.5 second pause
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    class Task2 implements Runnable {
        public void run() {
            while (true) {
                synchronized (hashSet) {
                    if (hashSet.size() <= 1) {
                        break; // Stop when only one letter remains
                    }
                    Iterator<Character> iterator = hashSet.iterator();
                    while (iterator.hasNext()) {
                        System.out.print(iterator.next() + " ");
                    }
                    System.out.println();
                }
                try {
                    Thread.sleep(300); // Wait 0.3 seconds
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    public static void main(String[] args) {
        HashSetSynchronizedFinal hs = new HashSetSynchronizedFinal();
        for (char letter = 'A'; letter <= 'Z'; letter++) {
            hs.hashSet.add(letter);
        }

        //Insert your code to
        // 1. Print the initial set using an iterator
        System.out.println("Initial set: ");
        synchronized (hs.hashSet) {
            Iterator<Character> iterator = hs.hashSet.iterator();
            while (iterator.hasNext()) {
                System.out.print(iterator.next() + " ");
            }
            System.out.println();
        }
        // 2. Print a header before starting your threads
        System.out.println("Initializing Threads...");

        hs.start();

        // Ensure threads complete
        try {
            hs.thread1.join();
            hs.thread2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Print a complete message
        System.out.println("Characters Removed.");

        // Print any remaining characters in the set
        // Should only have Z
        System.out.print("Remaining set: ");
        synchronized (hs.hashSet) {
            Iterator<Character> iterator = hs.hashSet.iterator();
            while (iterator.hasNext()) {
                System.out.print(iterator.next() + " ");
            }
            System.out.println();
        }
    }


    // YOU MAY NOT CHANGE ANY OF THE REMAINING CODE
    public HashSetSynchronizedFinal() {
        thread1 = new Thread(new Task1());
        thread2 = new Thread(new Task2());
    }

    private void start() {
        thread1.start();
        thread2.start();
    }



}
