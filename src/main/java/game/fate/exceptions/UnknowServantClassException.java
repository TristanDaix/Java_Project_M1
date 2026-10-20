package game.fate.exceptions;

/**
 * Thrown to indicate that the application attempted to instantiate or parse a
 * Servant possessing an undefined or invalid ServantClass literal.
 */
public class UnknowServantClassException extends RuntimeException {
    /**
     * Constructs a new exception with the specified detail message indicating the invalid class string.
     *
     * @param message The detail message containing the unrecognized class name.
     */
    public UnknowServantClassException(String message) {
        super(message);
    }
}
