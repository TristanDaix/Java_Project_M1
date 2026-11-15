package game.fate.services;

import game.fate.exceptions.NotEnoughServantsException;
import game.fate.models.dto.ActionResult;
import game.fate.models.dto.LevelUpResult;
import game.fate.models.servant.Servant;
import game.fate.models.servant.ServantClass;
import game.fate.testutils.ConsoleTestSupport;
import game.fate.testutils.TestServants;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ConsoleManager, the presentation layer.
 */
public class ConsoleManagerTest {
    private ConsoleTestSupport io;

    /**
     * Gives the real System.in / System.out back after each test.
     */
    @AfterEach
    void tearDown() {
        if (io != null) {
            io.close();
        }
    }

    private ConsoleManager consoleTyping(String keyboardInput) {
        io = new ConsoleTestSupport(keyboardInput);
        return io.createConsole();
    }

    private ActionResult result(String source, String target, String action, int targetHp, int sourceHp,
                                int sourceMp, int targetMp, boolean critical, boolean defeated, String message) {
        return new ActionResult(source, target, action, targetHp, sourceHp, sourceMp, targetMp, critical, defeated, message);
    }

    private List<Servant> twoServants() {
        return List.of(
                TestServants.create("Artoria", ServantClass.SABER, 1200, 100, 100),
                TestServants.create("Emiya", ServantClass.ARCHER, 1000, 90, 95));
    }

    // ------------------------------------------------------------------ simple printing

    /**
     * An action dialogue is displayed after a > prefix.
     */
    @Test
    void printActionDialoguePrefixesTheTextWithAnArrow() {
        consoleTyping("").printActionDialogue("SaberLion eat Lancer");

        assertEquals("> SaberLion eat Lancer", io.output().strip());
    }

    /**
     * A system dialogue is displayed as > [SYSTEM] : "text".
     */
    @Test
    void printSystemDialogueUsesTheSystemFormat() {
        consoleTyping("").printSystemDialogue("Starting game...");

        assertEquals("> [SYSTEM] : \"Starting game...\"", io.output().strip());
    }

    /**
     * A character dialogue is displayed as > [Name] : "text".
     */
    @Test
    void printCharacterDialogueShowsTheSpeakerAndTheText() {
        consoleTyping("").printCharacterDialogue("Artoria", "Are you my master ?");

        assertEquals("> [Artoria] : \"Are you my master ?\"", io.output().strip());
    }

    /**
     * An option is displayed as (number) text.
     */
    @Test
    void printOptionShowsTheNumberInParentheses() {
        consoleTyping("").printOption(42, "Eat");

        assertEquals("(42) Eat", io.output().strip());
    }

    /**
     * The header is the title framed by two lines of equal signs.
     */
    @Test
    void printHeaderFramesTheTitle() {
        consoleTyping("").printHeader("BATTLE START");

        List<String> lines = io.output().lines().filter(line -> !line.isBlank()).toList();
        assertEquals(3, lines.size());
        assertEquals("=".repeat(40), lines.get(0));
        assertEquals("BATTLE START", lines.get(1));
        assertEquals("=".repeat(40), lines.get(2));
    }

    /**
     * The separator is a line of dashes.
     */
    @Test
    void printSeparatorShowsAHorizontalLine() {
        consoleTyping("").printSeparator();

        assertEquals("-".repeat(40), io.output().strip());
    }

    // ------------------------------------------------------------------ main menu

    /**
     * The main menu shows its title, the inspiration note and the two options.
     */
    @Test
    void mainMenuShowsTitleAndOptions() {
        consoleTyping("1\n").showMainMenu();

        String output = io.output();
        assertTrue(output.contains("ISEN HOLY GRAIL WAR - MAIN MENU"));
        assertTrue(output.contains("(1) Start game"));
        assertTrue(output.contains("(2) Quit game"));
    }

    /**
     * Typing 1 returns 1 (start the game).
     */
    @Test
    void mainMenuReturnsOneToStartTheGame() {
        assertEquals(1, consoleTyping("1\n").showMainMenu());
    }

    /**
     * Typing 2 returns 2 (quit the game).
     */
    @Test
    void mainMenuReturnsTwoToQuit() {
        assertEquals(2, consoleTyping("2\n").showMainMenu());
    }

    /**
     * Numbers other than 1 and 2 (0, 3, negative) are refused with "Invalid choice." until a valid one is typed.
     */
    @Test
    void mainMenuRefusesNumbersOutsideTheMenu() {
        int choice = consoleTyping("0\n3\n-1\n2\n").showMainMenu();

        assertEquals(2, choice);
        assertEquals(3, io.output().split("Invalid choice\\.", -1).length - 1);
    }

    /**
     * Text that is not a number is ignored and the menu keeps waiting for a number.
     */
    @Test
    void mainMenuIgnoresTextThatIsNotANumber() {
        int choice = consoleTyping("help\nstart\n1\n").showMainMenu();

        assertEquals(1, choice);
    }

    // ------------------------------------------------------------------ servant selection

    /**
     * The selection menu lists every servant with its summary (class, name, HP, MP, ATK).
     */
    @Test
    void selectionMenuListsEveryServant() {
        consoleTyping("1\n1\n").showSelectServantMenu(twoServants());

        String output = io.output();
        assertTrue(output.contains("SERVANT SELECTION MENU"));
        assertTrue(output.contains("(1) SABER Artoria (1200 HP, 100 MP, 100 ATK)"));
        assertTrue(output.contains("(2) ARCHER Emiya (1000 HP, 90 MP, 95 ATK)"));
    }

    /**
     * Choosing a servant and confirming returns that servant.
     */
    @Test
    void choosingAndConfirmingReturnsTheServant() {
        List<Servant> servants = twoServants();

        Servant chosen = consoleTyping("2\n1\n").showSelectServantMenu(servants);

        assertSame(servants.get(1), chosen);
        assertTrue(io.output().contains("[Emiya] : \"Are you my master ?\""));
        assertTrue(io.output().contains("Emiya is now your servant."));
    }

    /**
     * Answering "No" to the servant sends the player back to the choice, without leaving the menu.
     */
    @Test
    void refusingAServantAllowsChoosingAnother() {
        List<Servant> servants = twoServants();

        Servant chosen = consoleTyping("1\n2\n2\n1\n").showSelectServantMenu(servants);

        assertSame(servants.get(1), chosen);
        assertTrue(io.output().contains("Choose another servant."));
    }

    /**
     * Any answer other than 1 to the confirmation question counts as "No".
     */
    @Test
    void anyConfirmationAnswerOtherThanOneMeansNo() {
        List<Servant> servants = twoServants();

        Servant chosen = consoleTyping("1\n7\n1\n1\n").showSelectServantMenu(servants);

        assertSame(servants.getFirst(), chosen);
        assertTrue(io.output().contains("Choose another servant."));
    }

    /**
     * Numbers outside the list are refused with "Invalid choice.".
     */
    @Test
    void selectionRefusesNumbersOutsideTheList() {
        List<Servant> servants = twoServants();

        Servant chosen = consoleTyping("0\n3\n1\n1\n").showSelectServantMenu(servants);

        assertSame(servants.getFirst(), chosen);
        assertTrue(io.output().contains("Invalid choice."));
    }

    /**
     * Edge case: the last servant of the list can be chosen.
     */
    @Test
    void theLastServantOfTheListCanBeChosen() {
        List<Servant> servants = twoServants();

        Servant chosen = consoleTyping("2\n1\n").showSelectServantMenu(servants);

        assertSame(servants.get(1), chosen);
    }

    /**
     * An empty list cannot be shown: a NotEnoughServantsException is thrown.
     */
    @Test
    void selectionFailsWhenThereIsNoServant() {
        ConsoleManager console = consoleTyping("");

        NotEnoughServantsException exception = assertThrows(NotEnoughServantsException.class,
                () -> console.showSelectServantMenu(List.of()));

        assertNotNull(exception.getMessage());
    }

    // ------------------------------------------------------------------ combat menu

    /**
     * The combat menu lists every action with its MP cost.
     */
    @Test
    void combatMenuListsEveryActionWithItsCost() {
        Servant servant = TestServants.servantWithAction();

        consoleTyping("1\n").showCombatMenu(servant.getAvailableActions(), servant);

        String output = io.output();
        assertTrue(output.contains("Your turn. Select an action:"));
        assertTrue(output.contains("(1) Attack [0 MP]"));
        assertTrue(output.contains("(2) Defense [0 MP]"));
        assertTrue(output.contains("(3) Break [0 MP]"));
        assertTrue(output.contains("(4) Skill: Mana Burst [20 MP]"));
        assertTrue(output.contains("(5) NP: Excalibur [50 MP]"));
    }

    /**
     * The menu returns the number typed by the player, for an affordable action.
     */
    @Test
    void combatMenuReturnsTheChosenNumber() {
        Servant servant = TestServants.servantWithAction();

        int choice = consoleTyping("3\n").showCombatMenu(servant.getAvailableActions(), servant);

        assertEquals(3, choice);
    }

    /**
     * Numbers outside the menu are refused with "Invalid choice.".
     */
    @Test
    void combatMenuRefusesNumbersOutsideTheMenu() {
        Servant servant = TestServants.servantWithAction();

        int choice = consoleTyping("0\n6\n2\n").showCombatMenu(servant.getAvailableActions(), servant);

        assertEquals(2, choice);
        assertTrue(io.output().contains("Invalid choice."));
    }

    /**
     * An action whose cost is above the current MP is refused ("Not enough MP for that action!").
     */
    @Test
    void combatMenuRefusesAnActionTheServantCannotAfford() {
        Servant servant = TestServants.servantWithAction();
        servant.consumeMp(51);

        int choice = consoleTyping("5\n1\n").showCombatMenu(servant.getAvailableActions(), servant);

        assertEquals(1, choice);
        assertTrue(io.output().contains("Not enough MP for that action!"));
    }

    /**
     * Text that is not a number is ignored and the menu keeps waiting for a number.
     */
    @Test
    void combatMenuIgnoresTextThatIsNotANumber() {
        Servant servant = TestServants.servantWithAction();

        int choice = consoleTyping("attack\n1\n").showCombatMenu(servant.getAvailableActions(), servant);

        assertEquals(1, choice);
    }

    // ------------------------------------------------------------------ combat status

    /**
     * The status shows the turn number and both servants with class, current HP, current MP and ATK.
     */
    @Test
    void combatStatusShowsTurnAndBothServants() {
        List<Servant> servants = twoServants();

        consoleTyping("").printCombatStatus(4, servants.getFirst(), servants.get(1));

        String output = io.output();
        assertTrue(output.contains("TURN 4"));
        assertTrue(output.contains("[YOU] Artoria (SABER)"));
        assertTrue(output.contains("1200 HP"));
        assertTrue(output.contains("100 MP"));
        assertTrue(output.contains("100 ATK"));
        assertTrue(output.contains("VS"));
        assertTrue(output.contains("[ENEMY] Emiya (ARCHER)"));
        assertTrue(output.contains("1000 HP"));
        assertTrue(output.contains("90 MP"));
        assertTrue(output.contains("95 ATK"));
    }

    /**
     * The status shows the current HP and MP, not the maximums.
     */
    @Test
    void combatStatusShowsCurrentHpAndMp() {
        List<Servant> servants = twoServants();
        servants.getFirst().takeDamage(200);
        servants.getFirst().consumeMp(30);

        consoleTyping("").printCombatStatus(1, servants.getFirst(), servants.get(1));

        assertTrue(io.output().contains("1000 HP"));
        assertTrue(io.output().contains("70 MP"));
        assertFalse(io.output().contains("1200 HP"));
    }

    // ------------------------------------------------------------------ action result

    /**
     * An action on another servant is announced "Source used Action on Target!".
     */
    @Test
    void actionResultAnnouncesTheActionOnItsTarget() {
        consoleTyping("").printActionResult(result("Artoria", "Emiya", "Attack", 0, 0, 0, 0, false, false, null));

        assertTrue(io.output().contains("Artoria used Attack on Emiya!"));
    }

    /**
     * An action without target (null target name) is announced "Source used Action!".
     */
    @Test
    void actionResultAnnouncesAnActionWithoutTarget() {
        consoleTyping("").printActionResult(result("Medea", null, "Skill: High-Speed Divine Words", 0, 0, 50, 0, false, false, null));

        assertTrue(io.output().contains("Medea used Skill: High-Speed Divine Words!"));
        assertFalse(io.output().contains(" on "));
    }

    /**
     * The special message is displayed after the announcement.
     */
    @Test
    void actionResultShowsTheSpecialMessage() {
        consoleTyping("").printActionResult(result("Artoria", "Emiya", "NP: Excalibur", 0, 0, 0, 0, false, false, "EXCALIBUR!!!!!!!"));

        String output = io.output();
        assertTrue(output.contains("EXCALIBUR!!!!!!!"));
        assertTrue(output.indexOf("used NP: Excalibur") < output.indexOf("EXCALIBUR!!!!!!!"));
    }

    /**
     * A critical result displays "CRITICAL HIT !".
     */
    @Test
    void actionResultShowsACriticalHit() {
        consoleTyping("").printActionResult(result("Artoria", "Emiya", "Attack", -200, 0, 0, 0, true, false, null));

        assertTrue(io.output().contains("CRITICAL HIT !"));
    }

    /**
     * A non-critical result does not display "CRITICAL HIT !".
     */
    @Test
    void actionResultHidesTheCriticalHitWhenNotCritical() {
        consoleTyping("").printActionResult(result("Artoria", "Emiya", "Attack", -100, 0, 0, 0, false, false, null));

        assertFalse(io.output().contains("CRITICAL"));
    }

    /**
     * A negative health change of the target is displayed as damage.
     */
    @Test
    void actionResultShowsTheDamageTakenByTheTarget() {
        consoleTyping("").printActionResult(result("Artoria", "Emiya", "Attack", -120, 0, 0, 0, false, false, null));

        assertTrue(io.output().contains("Emiya took 120 damage."));
    }

    /**
     * A positive health change of the target is displayed as healing.
     */
    @Test
    void actionResultShowsTheHealingOfTheTarget() {
        consoleTyping("").printActionResult(result("Artoria", "Emiya", "Heal", 30, 0, 0, 0, false, false, null));

        assertTrue(io.output().contains("Emiya was healed for 30 HP."));
    }

    /**
     * A negative health change of the source is displayed as recoil damage.
     */
    @Test
    void actionResultShowsTheRecoilDamageOfTheSource() {
        consoleTyping("").printActionResult(result("Heracles", "Emiya", "Skill: Valor", -220, -100, -15, 0, false, false, null));

        assertTrue(io.output().contains("Heracles took 100 recoil damage."));
    }

    /**
     * A positive health change of the source is displayed as recovery.
     */
    @Test
    void actionResultShowsTheRecoveryOfTheSource() {
        consoleTyping("").printActionResult(result("Medusa", "Emiya", "NP: Bellerophon", -250, 125, -40, 0, false, false, null));

        assertTrue(io.output().contains("Medusa recovered 125 HP."));
    }

    /**
     * A negative MP change of the source is displayed as MP consumed.
     */
    @Test
    void actionResultShowsTheMpConsumedBySource() {
        consoleTyping("").printActionResult(result("Artoria", "Emiya", "NP: Excalibur", -350, 0, -50, 0, false, false, null));

        assertTrue(io.output().contains("Artoria consumed 50 MP."));
    }

    /**
     * A positive MP change of the source is displayed as MP restored.
     */
    @Test
    void actionResultShowsTheMpRestoredBySource() {
        consoleTyping("").printActionResult(result("Medea", null, "Skill: High-Speed Divine Words", 0, 0, 50, 0, false, false, null));

        assertTrue(io.output().contains("Medea restored 50 MP."));
    }

    /**
     * A negative MP change of the target is displayed as MP lost.
     */
    @Test
    void actionResultShowsTheMpLostByTarget() {
        consoleTyping("").printActionResult(result("Medea", "Emiya", "NP: Rule Breaker", -150, 0, -10, -30, false, false, null));

        assertTrue(io.output().contains("Emiya lost 30 MP."));
    }

    /**
     * A positive MP change of the target is displayed as MP gained.
     */
    @Test
    void actionResultShowsTheMpGainedByTarget() {
        consoleTyping("").printActionResult(result("Medea", "Emiya", "Gift", 0, 0, 0, 10, false, false, null));

        assertTrue(io.output().contains("Emiya gained 10 MP."));
    }

    /**
     * A defeated target is announced "has been defeated !".
     */
    @Test
    void actionResultAnnouncesTheDefeatOfTheTarget() {
        consoleTyping("").printActionResult(result("Artoria", "Emiya", "Attack", -500, 0, 0, 0, false, true, null));

        assertTrue(io.output().contains("Emiya has been defeated !"));
    }

    /**
     * A target that is not defeated is not announced as defeated.
     */
    @Test
    void actionResultDoesNotAnnounceADefeatWhenTheTargetSurvives() {
        consoleTyping("").printActionResult(result("Artoria", "Emiya", "Attack", -50, 0, 0, 0, false, false, null));

        assertFalse(io.output().contains("defeated"));
    }

    // ------------------------------------------------------------------ level up

    /**
     * Nothing is displayed when the experience gain did not lead to a level up.
     */
    @Test
    void levelUpPrintsNothingWithoutLevelUp() {
        consoleTyping("").printLevelUp(new LevelUpResult(false, 1, 1, 100, 100, 1000, 1000, 100, 100));

        assertEquals("", io.output());
    }

    /**
     * A level up displays the new level and the before/after values of ATK, HP and MP.
     */
    @Test
    void levelUpShowsNewLevelAndStatChanges() {
        consoleTyping("").printLevelUp(new LevelUpResult(true, 1, 2, 100, 120, 1000, 1200, 80, 96));

        String output = io.output();
        assertTrue(output.contains("LEVEL UP! Reached Level 2!"));
        assertTrue(output.contains("ATK: 100 -> 120"));
        assertTrue(output.contains("HP: 1000 -> 1200"));
        assertTrue(output.contains("MP: 80 -> 96"));
    }
}
