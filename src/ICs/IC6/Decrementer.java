/**
 * Class: Decrementer
 * Author: Hugo Padilla
 * Version: 1.0
 * Course: ITEC 3150 Fall 2024
 * Written: 29 October 24
 *
 * This class – Decrementer, implements the Runnable interface and is responsible for decreasing the
 * score value in the Score class by a specific amount. It is intended to be run in a multi-threaded
 * environment, allowing concurrent decrement operations on the Score object.
 */
package ICs.IC6;
public class Decrementer implements Runnable {
    private Score score;

    public Decrementer (Score score){
        this.score = score;
    }
    @Override
    public void run() {
        score.updateScore((-5));
    }
}
