package game.fate.exceptions;

/**
 * Thrown when the application fails to access the pattern of a servant
 * required to initialize the game or populate an enemy roster.
 */
public class UnavailablePatternException extends RuntimeException {
    /**
     * Constructs a new exception with the specified detail message detailing the shortage.
     *
     * @param message The detail message explaining the context of the error.
     */
    public UnavailablePatternException(String message) {
        super(message);
    }
}
