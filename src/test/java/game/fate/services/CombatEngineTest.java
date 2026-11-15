package game.fate.services;

import game.fate.models.actions.ActionType;
import game.fate.models.dto.ActionResult;
import game.fate.models.servant.Servant;
import game.fate.models.servant.ServantClass;
import game.fate.testutils.RecordingAction;
import game.fate.testutils.ServantClassSnapshot;
import game.fate.testutils.TestServants;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for CombatEngine, the class that resolves one turn between two servants.
 */
public class CombatEngineTest {
    private ServantClassSnapshot snapshot;
    private CombatEngine engine;
    private Servant first;
    private Servant second;

    /**
     * Neutral class stats, a fresh engine and two servants (1000 HP, 100 ATK) before each test.
     */
    @BeforeEach
    void setUp() {
        snapshot = ServantClassSnapshot.capture();
        ServantClassSnapshot.useNeutralStats();
        engine = new CombatEngine();
        first = TestServants.create("Ishtar", ServantClass.ARCHER, 1000, 100, 100);
        second = TestServants.create("Ereshkigal", ServantClass.LANCER, 1000, 100, 100);
    }

    /**
     * Restores the original class statistics.
     */
    @AfterEach
    void tearDown() {
        snapshot.restore();
    }

    /**
     * The engine always returns an array of two slots (the second one is empty when a rule resolved the clash).
     */
    @Test
    void alwaysReturnsAnArrayOfTwoSlots() {
        ActionResult[] results = engine.resolveTurn(first, new RecordingAction(ActionType.ATTACK),
                second, new RecordingAction(ActionType.BREAK));

        assertEquals(2, results.length);
    }

    /**
     * Attack against break: the attack wins, the break is not executed, and only one result is produced.
     */
    @Test
    void attackAgainstBreakIsResolvedByTheAttackRule() {
        RecordingAction attack = new RecordingAction(ActionType.ATTACK, "Attack", 0, 100);
        RecordingAction breakAction = new RecordingAction(ActionType.BREAK, "Break", 0, 100);

        ActionResult[] results = engine.resolveTurn(first, attack, second, breakAction);

        assertNotNull(results[0]);
        assertNull(results[1]);
        assertEquals(1, attack.getExecutionCount());
        assertEquals(0, breakAction.getExecutionCount());
        assertEquals(900, second.getHp());
        assertEquals(1000, first.getHp());
    }

    /**
     * Break against defense: the break wins, the defense is not executed, and only one result is produced.
     */
    @Test
    void breakAgainstDefenseIsResolvedByTheBreakRule() {
        RecordingAction breakAction = new RecordingAction(ActionType.BREAK, "Break", 0, 100);
        RecordingAction defense = new RecordingAction(ActionType.DEFENSE, "Defense", 0, 0);

        ActionResult[] results = engine.resolveTurn(first, breakAction, second, defense);

        assertNotNull(results[0]);
        assertNull(results[1]);
        assertEquals(1, breakAction.getExecutionCount());
        assertEquals(0, defense.getExecutionCount());
        assertEquals(900, second.getHp());
    }

    /**
     * Defense against attack: the defense is executed, the attack is blocked and the attacker takes a
     * counter-attack and only one result is produced.
     */
    @RepeatedTest(20)
    void defenseAgainstAttackIsResolvedByTheCounterAttackRule() {
        RecordingAction defense = new RecordingAction(ActionType.DEFENSE, "Defense", 0, 0);
        RecordingAction attack = new RecordingAction(ActionType.ATTACK, "Attack", 0, 500);

        ActionResult[] results = engine.resolveTurn(first, defense, second, attack);

        assertNotNull(results[0]);
        assertNull(results[1]);
        assertEquals(1, defense.getExecutionCount());
        assertEquals(0, attack.getExecutionCount());
        assertEquals(1000, first.getHp());
        assertTrue(second.getHp() >= 890 && second.getHp() <= 910, "counter-attack out of range: " + second.getHp());
    }

    private void assertBothActionsAreExecuted(ActionType firstType, ActionType secondType) {
        RecordingAction firstAction = new RecordingAction(firstType, "first", 0, 10);
        RecordingAction secondAction = new RecordingAction(secondType, "second", 0, 20);

        ActionResult[] results = engine.resolveTurn(first, firstAction, second, secondAction);

        assertNotNull(results[0]);
        assertNotNull(results[1]);
        assertEquals(1, firstAction.getExecutionCount());
        assertEquals(1, secondAction.getExecutionCount());
        assertEquals(990, second.getHp());
        assertEquals(980, first.getHp());
        assertSame(first, firstAction.getLastSource());
        assertSame(second, secondAction.getLastSource());
    }

    /**
     * Interaction with has no rule: both are executed.
     */
    @Test
    void ExecutesBothActions1() {
        assertBothActionsAreExecuted(ActionType.ATTACK, ActionType.ATTACK);
    }

    @Test
    void ExecutesBothActions2() {
        assertBothActionsAreExecuted(ActionType.BREAK, ActionType.BREAK);
    }

    @Test
    void ExecutesBothActions3() {
        assertBothActionsAreExecuted(ActionType.DEFENSE, ActionType.DEFENSE);
    }

    @Test
    void ExecutesBothActions4() {
        assertBothActionsAreExecuted(ActionType.NP, ActionType.NP);
    }

    @Test
    void ExecutesBothActions5() {
        assertBothActionsAreExecuted(ActionType.SKILL, ActionType.ATTACK);
    }

    @Test
    void ExecutesBothActions6() {
        assertBothActionsAreExecuted(ActionType.NP, ActionType.DEFENSE);
    }

    @Test
    void ExecutesBothActions7() {
        assertBothActionsAreExecuted(ActionType.BREAK, ActionType.SKILL);
    }
}
