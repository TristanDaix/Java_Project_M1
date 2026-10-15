package game.fate.exceptions;

/**
 * Throw to indicate that the game tried to instanciate a Servant with an unknow class
 */
public class UnknowServantClassException extends RuntimeException {
    public UnknowServantClassException(String message) {
        super(message);
    }
}
