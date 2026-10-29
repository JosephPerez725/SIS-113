/**
 * Same constructor and same public methods as the Clase 6 version, so Main
 * and Console compile unchanged. Everything shared moved up to Rule.
 */
public class MemoryRule extends Rule {

    public MemoryRule(int threshold) {
        super("memory", threshold);
    }

    @Override
    protected boolean matches(Snapshot reading) {
        return reading.getMemoryPercent() >= getThreshold();
    }
}
