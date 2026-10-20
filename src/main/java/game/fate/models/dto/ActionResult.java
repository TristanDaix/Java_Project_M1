package game.fate.models.dto;

/**
 * A data transfer object that encapsulates the outcome of a combat action.
 * Decouples the underlying battle math from the UI presentation layer.
 *
 * @param sourceName     The name of the entity performing the action.
 * @param targetName     The name of the entity receiving the action.
 * @param actionName     The name of the executed action.
 * @param healthChange   The net change in the target's health (negative for damage, positive for healing).
 * @param isCritical     Indicates if the action triggered a critical multiplier.
 * @param targetDefeated Indicates if the target's health reached zero as a direct result of this action.
 */
public record ActionResult(String sourceName, String targetName, String actionName, int healthChange,
                           boolean isCritical, boolean targetDefeated) {
}
