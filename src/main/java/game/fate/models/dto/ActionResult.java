package game.fate.models.dto;

/**
 * Encapsulates the outcome of an action so the UI can display it without knowing game logic.
 */
public record ActionResult(String sourceName, String targetName, String actionName, int healthChange,
                           boolean isCritical, boolean targetDefeated) {
}
