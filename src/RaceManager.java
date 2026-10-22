// ============================================================
// 2.2
// ============================================================

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Manages all horses in a race, tracks their positions,
 * and records the finishing order.
 *
 * Data Structure Justification:
 *
 * • List<Horse> horses — An ArrayList gives O(1) indexed access
 *   and is easy to iterate when printing live standings.
 *   Order of insertion = starting lineup, which never changes.
 *
 * • List<Horse> finishingOrder — A synchronizedList-wrapped ArrayList
 *   lets multiple horse threads safely append to the list the moment
 *   each horse crosses the finish line, preserving the true
 *   finishing sequence without corrupting the structure.
 */
public class RaceManager {

    private static final int FINISH_LINE = 100; // the distance to complete the race

    // All horses entered in the race (set once, read-only during race)
    private final List<Horse> horses;

    // Records horses in the order they cross the finish line.
    // Collections.synchronizedList wraps ArrayList so that concurrent
    // horse threads can add entries without data corruption.
    private final List<Horse> finishingOrder;

    /** Constructs a RaceManager with an empty field. */
    public RaceManager() {
        this.horses       = new ArrayList<>();
        this.finishingOrder = Collections.synchronizedList(new ArrayList<>());
    }

    /**
     * Adds a horse to the race.
     *
     * @param horse the horse to register
     */
    public void addHorse(Horse horse) {
        horses.add(horse);
    }

    /** @return an unmodifiable view of all registered horses */
    public List<Horse> getHorses() {
        return Collections.unmodifiableList(horses);
    }

    /** @return the target distance every horse must reach */
    public int getFinishLine() {
        return FINISH_LINE;
    }

    /**
     * Called by a horse's thread when it crosses the finish line.
     * Thread-safe: the underlying list is a synchronizedList.
     *
     * @param horse the horse that just finished
     */
    public void recordFinish(Horse horse) {
        finishingOrder.add(horse);
        System.out.println(horse.getName() + " has finished!");
    }

    /** @return the finishing-order list (synchronized) */
    public List<Horse> getFinishingOrder() {
        return finishingOrder;
    }

    // 2.5

    /**
     * Displays the finishing order and final distance of every horse.
     * Called only after all threads have joined (race is fully over),
     * so no synchronization is needed here — results are stable.
     */
    public void displayResults() {
        System.out.println("\nFINAL RESULTS:");
        // finishingOrder already holds horses in completion sequence
        for (int i = 0; i < finishingOrder.size(); i++) {
            Horse h = finishingOrder.get(i);
            System.out.println((i + 1) + ". " + h.getName()
                    + " (Distance: " + h.getPosition() + ")");
        }
    }
}