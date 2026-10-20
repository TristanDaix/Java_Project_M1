package game.fate.exceptions;

/**
 * Thrown when the application fails to load or access a sufficient number of
 * unique servants required to initialize the game or populate an enemy roster.
 */
public class NotEnoughServantsException extends RuntimeException {
    /**
     * Constructs a new exception with the specified detail message detailing the shortage.
     *
     * @param message The detail message explaining the context of the error.
     */
    public NotEnoughServantsException(String message) {
        super(message);
    }
}
