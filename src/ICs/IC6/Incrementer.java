/**
 * Class: Incrementer
 * Author: Hugo Padilla
 * Version: 1.0
 * Course: ITEC 3150 Fall 2024
 * Written: 29 October 24
 *
 * This class – Incrementer, implements the Runnable interface and is responsible for increasing the
 * score value in the Score class by a specific amount. It is designed to be used in a multi-threaded
 * environment, allowing concurrent increment operations on the Score object.
 */
package ICs.IC6;
public class Incrementer implements Runnable {
    private Score score;

    public Incrementer(Score score) {
        this.score = score;
    }

    @Override
    public void run() {
            score.updateScore(5);
    }
}
