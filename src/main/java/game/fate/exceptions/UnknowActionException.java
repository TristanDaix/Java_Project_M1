package game.fate.exceptions;

/**
 * Thrown to indicate that the application attempted to instantiate an invalid action
 */
public class UnknowActionException extends RuntimeException {
    /**
     * Constructs a new exception with the specified detail message indicating the invalid action string.
     *
     * @param message The detail message containing the unrecognized action name.
     */
    public UnknowActionException(String message) {
        super(message);
    }
}
