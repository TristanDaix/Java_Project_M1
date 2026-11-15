package game.fate.models.servant;

import game.fate.testutils.ServantClassSnapshot;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the ServantClass enumeration.
 * The enum values are mutable, so every test restores the original statistics afterward to avoid polluting the other test classes.
 */
public class ServantClassTest {
    private ServantClassSnapshot snapshot;

    /**
     * Saves the statistics of all classes before each test.
     */
    @BeforeEach
    void saveClassStats() {
        snapshot = ServantClassSnapshot.capture();
    }

    /**
     * Restores the statistics of all classes after each test.
     */
    @AfterEach
    void restoreClassStats() {
        snapshot.restore();
    }

    /**
     * The seven classes of the Holy Grail War are declared in the traditional order.
     */
    @Test
    void declaresTheSevenClassesInOrder() {
        ServantClass[] expected = {ServantClass.SABER, ServantClass.ARCHER, ServantClass.LANCER, ServantClass.RIDER,
                ServantClass.CASTER, ServantClass.ASSASSIN, ServantClass.BERSERKER};

        assertArrayEquals(expected, ServantClass.values());
    }

    /**
     * Before any CSV is loaded, the sort order of the classes is 1 to 7 following the declaration order.
     */
    @Test
    void defaultSortOrdersGoFromOneToSeven() {
        assertEquals(1, ServantClass.SABER.getSortOrder());
        assertEquals(2, ServantClass.ARCHER.getSortOrder());
        assertEquals(3, ServantClass.LANCER.getSortOrder());
        assertEquals(4, ServantClass.RIDER.getSortOrder());
        assertEquals(5, ServantClass.CASTER.getSortOrder());
        assertEquals(6, ServantClass.ASSASSIN.getSortOrder());
        assertEquals(7, ServantClass.BERSERKER.getSortOrder());
    }

    /**
     * Default combat multipliers.
     */
    @Test
    void DefaultMultipliers() {
        assertEquals(1.0, ServantClass.SABER.getAttackMultiplier());
        assertEquals(1.0, ServantClass.SABER.getCriticalMultiplier());
        assertEquals(0.0, ServantClass.SABER.getCriticalRate());
    }

    /**
     * setStats replaces the four statistics of the class.
     */
    @Test
    void setStatsOverwritesTheFourValues() {
        ServantClass.ARCHER.setStats(9, 1.5, 2.5, 0.4);

        assertEquals(9.0, ServantClass.ARCHER.getSortOrder());
        assertEquals(1.5, ServantClass.ARCHER.getAttackMultiplier());
        assertEquals(2.5, ServantClass.ARCHER.getCriticalMultiplier());
        assertEquals(0.4, ServantClass.ARCHER.getCriticalRate());
    }

    /**
     * Resolves a constant from its exact name.
     */
    @Test
    void findsAClassByName() {
        assertEquals(ServantClass.BERSERKER, ServantClass.valueOf("BERSERKER"));
    }

    /**
     * Rejects unknown names or badly cased
     */
    @Test
    void rejectsUnknownOrBadlyCasedNames() {
        assertThrows(IllegalArgumentException.class, () -> ServantClass.valueOf("GATEKEEPER"));
        assertThrows(IllegalArgumentException.class, () -> ServantClass.valueOf("saber"));
    }

    /**
     * Returns the constant name.
     */
    @Test
    void returnsTheConstantName() {
        assertEquals("LANCER", ServantClass.LANCER.toString());
    }
}
