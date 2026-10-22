// ============================================================
// 2.3
// ============================================================

import java.util.Random;

/**
 * Runnable that simulates one horse running the race.
 *
 * Concurrency design:
 * ───────────────────
 * • Each horse runs in its own Thread — all horses advance simultaneously.
 * • Random step sizes (1–10 units) and random sleep intervals (100–500 ms)
 *   simulate realistic, unpredictable race movement.
 * • Horse.move() uses AtomicInteger internally, so position updates
 *   from different threads never interfere with each other.
 * • RaceManager.recordFinish() uses a synchronizedList, preventing
 *   two horses from corrupting the finishing-order list if they
 *   cross the line at the exact same millisecond.
 */
public class HorseRunnable implements Runnable {

    private final Horse horse;
    private final RaceManager raceManager;
    private final Random random;

    /**
     * @param horse       the horse this thread controls
     * @param raceManager shared race state (finish line + finishing order)
     */
    public HorseRunnable(Horse horse, RaceManager raceManager) {
        this.horse       = horse;
        this.raceManager = raceManager;
        this.random      = new Random();
    }

    @Override
    public void run() {
        int finishLine = raceManager.getFinishLine();

        // Keep running until the horse reaches or passes the finish line
        while (horse.getPosition() < finishLine) {

            // Advance by a random number of steps (1 to 10)
            int steps = random.nextInt(10) + 1;
            horse.move(steps);



            // Simulate the time it takes to run those steps (100–500 ms)
            try {
                Thread.sleep(random.nextInt(400) + 100);
            } catch (InterruptedException e) {
                // Restore interrupt flag and exit cleanly if interrupted
                Thread.currentThread().interrupt();
                return;
            }
        }

        // Horse has crossed the finish line and records its place
        raceManager.recordFinish(horse);
    }
}