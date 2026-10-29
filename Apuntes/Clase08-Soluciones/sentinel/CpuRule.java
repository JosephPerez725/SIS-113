/** The rule that, in Clase 6, students wrote by copying MemoryRule. Now it is six lines. */
public class CpuRule extends Rule {

    public CpuRule(int threshold) {
        super("cpu", threshold);
    }

    @Override
    protected boolean matches(Snapshot reading) {
        return reading.getCpuPct() >= getThreshold();
    }
}
