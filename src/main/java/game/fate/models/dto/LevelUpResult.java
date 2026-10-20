package game.fate.models.dto;

public record LevelUpResult(boolean leveledUp, int oldLevel, int newLevel, int oldAtk, int newAtk, int oldHp,
                            int newHp) {
}
