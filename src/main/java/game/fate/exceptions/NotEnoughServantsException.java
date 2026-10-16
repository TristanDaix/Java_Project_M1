package game.fate.exceptions;

/**
 * Throw because not enough servant are present in the list to launch the game
 */
public class NotEnoughServantsException extends RuntimeException {
    public NotEnoughServantsException(String message) {
        super(message);
    }
}
