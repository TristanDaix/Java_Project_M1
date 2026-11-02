package game.fate.models.dto;

/**
 * A data transfer object that encapsulates the statistical changes resulting from an experience gain event.
 *
 * @param leveledUp True if the gained experience crossed a level threshold.
 * @param oldLevel  The servant's level before the experience was added.
 * @param newLevel  The servant's level after the experience was added.
 * @param oldAtk    The servant's attack stat before leveling up.
 * @param newAtk    The servant's attack stat after leveling up.
 * @param oldHp     The servant's max health stat before leveling up.
 * @param newHp     The servant's max health stat after leveling up.
 * @param oldMp     The servant's max mana point stat before leveling up.
 * @param newMp     The servant's max mana point stat after leveling up.
 */
public record LevelUpResult(boolean leveledUp, int oldLevel, int newLevel, int oldAtk, int newAtk, int oldHp,
                            int newHp, int oldMp, int newMp) {
}
