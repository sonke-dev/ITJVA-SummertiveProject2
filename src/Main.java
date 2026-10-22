// ============================================================
// 2.4
// ============================================================

import java.util.ArrayList;
import java.util.List;

/**
 * Entry point for the Horse Racing Simulation.
 *
 * Execution flow:
 * 1. Create horses and register them with RaceManager.
 * 2. Create one Thread per horse (backed by HorseRunnable).
 * 3. Start ALL threads at once  →  horses run simultaneously.
 * 4. Join ALL threads           →  main thread blocks until every
 *                                   horse has crossed the finish line.
 * 5. Display final results via RaceManager.displayResults().
 */
public class Main {

    public static void main(String[] args) {

        // Set up race
        RaceManager raceManager = new RaceManager();

        // Register horses (mirrors the sample output names)
        String[] horseNames = {"Thunder", "Blaze", "Storm", "Flash"};
        for (String name : horseNames) {
            raceManager.addHorse(new Horse(name));
        }

        // Creating threads
        // One Thread per horse so they all run at the sametime.
        List<Thread> threads = new ArrayList<>();
        for (Horse horse : raceManager.getHorses()) {
            Thread t = new Thread(new HorseRunnable(horse, raceManager));
            threads.add(t);
        }

        System.out.println(" THE RACE HAS STARTED!!!\n");

        // Start all threads simultaneously
        // Starting in a separate loop
        // ensures no thread gets a head start

        for (Thread t : threads) {
            t.start();
        }

        //  Waiting for every horse to finish
        // Thread.join() causes the main thread to block until the
        // joined thread terminates.
        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("RACE INTERRUPTED!!!");
                return;
            }
        }

        //  Display results (2.5)

        raceManager.displayResults();
    }
}