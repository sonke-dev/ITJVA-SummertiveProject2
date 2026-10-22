// ============================================================
// 2.1
// ============================================================

/**
 * Represents a racing horse with a name and current position.
 * Uses AtomicInteger for thread-safe position updates since
 * multiple threads (one per horse) will access this concurrently.
 */
public class Horse {

    private final String name;
    // AtomicInteger ensures position reads/writes are thread-safe

    private final java.util.concurrent.atomic.AtomicInteger position;

    /**
     * Constructs a Horse with the given name, starting at position 0.
     *
     * @param name the horse's name
     */
    public Horse(String name) {
        this.name = name;
        this.position = new java.util.concurrent.atomic.AtomicInteger(0);
    }

    /** @return the horse's name */
    public String getName() {
        return name;
    }

    /** @return the horse's current position (distance covered) */
    public int getPosition() {
        return position.get();
    }

    /**
     * Moves the horse forward by the given number of units.
     * Uses AtomicInteger.addAndGet() which is an atomic (indivisible)
     * operation — no race condition can corrupt the value.
     *
     * @param steps number of units to advance
     */
    public void move(int steps) {
        position.addAndGet(steps);
    }

    @Override
    public String toString() {
        return name + " (Distance: " + position.get() + ")";
    }
}