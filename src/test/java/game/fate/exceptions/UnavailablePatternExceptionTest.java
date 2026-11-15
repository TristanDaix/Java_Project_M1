package game.fate.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit tests for UnavailablePatternException, the custom exception raised when the combat pattern of a servant cannot be found.
 */
public class UnavailablePatternExceptionTest {
    /**
     * The exception can be thrown and caught by its own type, with its message intact.
     */
    @Test
    void canBeThrownAndCaughtByItsOwnType() {
        UnavailablePatternException caught = assertThrows(UnavailablePatternException.class, () -> {
            throw new UnavailablePatternException("No pattern for servant 69");
        });

        assertEquals("No pattern for servant 69", caught.getMessage());
    }
}
