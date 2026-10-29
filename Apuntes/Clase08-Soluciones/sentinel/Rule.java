/**
 * What every Sentinel rule shares: a name, a threshold, a hit counter, and
 * the guard that keeps the threshold between 1 and 100.
 *
 * Each child only answers one question: does this reading match?
 */
public abstract class Rule {

    private final String name;
    private final int threshold;
    private int hits = 0;

    protected Rule(String name, int threshold) {
        if (threshold < 1 || threshold > 100) {
            throw new IllegalArgumentException("threshold must be between 1 and 100, got " + threshold);
        }
        this.name = name;
        this.threshold = threshold;
    }

    /**
     * final: no child can change HOW hits are counted. They can only change
     * WHAT matches. This is the line that protects every rule from a
     * careless subclass.
     */
    public final boolean evaluate(Snapshot reading) {
        boolean alert = matches(reading);
        if (alert) {
            hits++;
        }
        return alert;
    }

    /** The only thing a child must write. */
    protected abstract boolean matches(Snapshot reading);

    public String getName() {
        return name;
    }

    public int getThreshold() {
        return threshold;
    }

    public int getHits() {
        return hits;
    }
}
