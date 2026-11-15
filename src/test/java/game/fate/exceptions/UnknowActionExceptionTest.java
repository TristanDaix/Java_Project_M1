package game.fate.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit tests for UnknowActionException, the custom exception raised when an action name read from a file is unknown.
 */
public class UnknowActionExceptionTest {
    /**
     * The exception can be thrown and caught by its own type, with its message intact.
     */
    @Test
    void canBeThrownAndCaughtByItsOwnType() {
        UnknowActionException caught = assertThrows(UnknowActionException.class, () -> {
            throw new UnknowActionException("PutANoteOf20/20");
        });

        assertEquals("PutANoteOf20/20", caught.getMessage());
    }
}
