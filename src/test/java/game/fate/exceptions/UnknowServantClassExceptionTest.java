package game.fate.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit tests for UnknowServantClassException, the custom exception raised when a servant class name read from a file is unknown.
 */
public class UnknowServantClassExceptionTest {
    /**
     * The exception can be thrown and caught by its own type, with its message intact.
     */
    @Test
    void canBeThrownAndCaughtByItsOwnType() {
        UnknowServantClassException caught = assertThrows(UnknowServantClassException.class, () -> {
            throw new UnknowServantClassException("WATCHER");
        });

        assertEquals("WATCHER", caught.getMessage());
    }
}
