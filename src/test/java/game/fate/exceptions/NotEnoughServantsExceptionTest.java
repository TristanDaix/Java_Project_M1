package game.fate.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit tests for NotEnoughServantsException, the custom exception raised when a list of servants is too small to start the game.
 */
public class NotEnoughServantsExceptionTest {
    /**
     * The exception can be thrown and caught by its own type, with its message intact.
     */
    @Test
    void canBeThrownAndCaughtByItsOwnType() {
        NotEnoughServantsException caught = assertThrows(NotEnoughServantsException.class, () -> {
            throw new NotEnoughServantsException("Not enough servants");
        });

        assertEquals("Not enough servants", caught.getMessage());
    }
}
