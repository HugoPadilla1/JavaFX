/**
 * Class: ScoreRunner
 * Author: Hugo Padilla
 * Version: 1.0
 * Course: ITEC 3150 Fall 2024
 * Written:  29 October 2024
 *
 * This class – ScoreRunner, serves as the main entry point for demonstrating the functionality of the
 * Score, Incrementer, and Decrementer classes. It creates and manages multiple threads to increment
 * and decrement scores concurrently, showcasing thread-safe operations on shared Score instances.
 */
package ICs.IC6;
import java.util.ArrayList;
import java.util.List;

public class ScoreRunner {
    public static void main(String[] args) {
        Score score1 = new Score(1, 100.0);
        Score score2 = new Score(2, 100.0);

        System.out.println("Starting value for score 1\t" + score1.getScore());
        System.out.println("Starting value for score 2\t" + score2.getScore());

        // Create and start increment threads
        List<Thread> incrementThreads = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            incrementThreads.add(new Thread(new Incrementer(score1)));
            incrementThreads.add(new Thread(new Incrementer(score2)));
        }
        for (Thread t : incrementThreads) {
            t.start();
        }
        for (Thread t : incrementThreads) {
            try {
                t.join();  // Wait for all increment threads to finish
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        // Create and start decrement threads
        List<Thread> decrementThreads = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            decrementThreads.add(new Thread(new Decrementer(score1)));
            decrementThreads.add(new Thread(new Decrementer(score2)));
        }
        for (Thread t : decrementThreads) {
            t.start();
        }
        for (Thread t : decrementThreads) {
            try {
                t.join();  // Wait for all decrement threads to finish
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        System.out.println("Ending value for score 1\t" + score1.getScore());
        System.out.println("Ending value for score 2\t" + score2.getScore());
    }
}
