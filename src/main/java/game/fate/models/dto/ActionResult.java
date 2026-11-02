package game.fate.models.dto;

/**
 * Encapsulates the outcome of an action, including HP/MP changes and special narrative effects.
 *
 * @param sourceName         The entity performing the action.
 * @param targetName         The entity receiving the action.
 * @param actionName         The executed action.
 * @param targetHealthChange The health change applied to the target (negative for damage).
 * @param sourceHealthChange The health change applied to the source (e.g., lifesteal or self-damage).
 * @param sourceMpChange     The MP change applied to the source (usually the negative cost).
 * @param targetMpChange     The MP change applied to the target (e.g., MP drain).
 * @param isCritical         Whether the attack was a critical hit.
 * @param targetDefeated     Whether the target died.
 * @param specialMessage     An optional narrative string for unique effects.
 */
public record ActionResult(String sourceName, String targetName, String actionName,
                           int targetHealthChange, int sourceHealthChange,
                           int sourceMpChange, int targetMpChange,
                           boolean isCritical, boolean targetDefeated, String specialMessage) {
}
