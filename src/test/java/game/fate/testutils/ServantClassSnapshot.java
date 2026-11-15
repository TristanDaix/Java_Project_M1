package game.fate.testutils;

import game.fate.models.servant.ServantClass;

import java.util.EnumMap;
import java.util.Map;

/**
 * Saves and restores the statistics of every ServantClass.
 * ServantClass is an enum whose values are mutable global state and are overwritten by CsvReader.loadClasses.
 */
public final class ServantClassSnapshot {
    private final Map<ServantClass, double[]> saved = new EnumMap<>(ServantClass.class);

    private ServantClassSnapshot() {
    }

    /**
     * Records the current statistics of all servant classes.
     *
     * @return a snapshot that can be restored later
     */
    public static ServantClassSnapshot capture() {
        ServantClassSnapshot snapshot = new ServantClassSnapshot();
        for (ServantClass servantClass : ServantClass.values()) {
            snapshot.saved.put(servantClass, new double[]{
                    servantClass.getSortOrder(),
                    servantClass.getAttackMultiplier(),
                    servantClass.getCriticalMultiplier(),
                    servantClass.getCriticalRate()});
        }
        return snapshot;
    }

    /**
     * Puts every servant class back in the state it had when capture was called.
     */
    public void restore() {
        for (Map.Entry<ServantClass, double[]> entry : saved.entrySet()) {
            double[] values = entry.getValue();
            entry.getKey().setStats((int) values[0], values[1], values[2], values[3]);
        }
    }

    /**
     * Makes every class neutral: attack x1.0, critical multiplier x1.0, critical rate 0.0
     * (the sort order is kept). Damage formulas then only depend on the servant itself.
     */
    public static void useNeutralStats() {
        for (ServantClass servantClass : ServantClass.values()) {
            servantClass.setStats(servantClass.getSortOrder(), 1.0, 1.0, 0.0);
        }
    }
}
