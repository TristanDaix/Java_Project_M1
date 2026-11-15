package game.fate.utils;

import game.fate.models.actions.ActionType;
import game.fate.models.servant.Servant;
import game.fate.models.servant.ServantClass;
import game.fate.testutils.ConsoleTestSupport;
import game.fate.testutils.ServantClassSnapshot;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for CsvReader
 * Each test writes its own small CSV file in a temporary folder (so the tests do not depend on the real
 * files of src/main/resources) and reads it back.
 */
public class CsvReaderTest {
    private static final String CLASSES_HEADER = "className,sortOrder,attackMultiplier,criticalMultiplier,criticalRate";
    private static final String SERVANTS_HEADER = "id,name,servantClass,hp,mp,atk,criticalMultiplier,criticalRate";
    private static final String PATTERNS_HEADER = "servantId,action1,action2,action3,action4";

    @TempDir
    Path tempDir;

    private ServantClassSnapshot snapshot;
    private ConsoleTestSupport io;

    /**
     * Saves the class statistics, gives every class a known sort order (SABER 1 ... BERSERKER 7) and
     * captures the standard streams.
     */
    @BeforeEach
    void setUp() {
        snapshot = ServantClassSnapshot.capture();
        for (ServantClass servantClass : ServantClass.values()) {
            servantClass.setStats(servantClass.ordinal() + 1, 1.0, 1.0, 0.0);
        }
        io = new ConsoleTestSupport("");
    }

    /**
     * Restores the standard streams and the class statistics.
     */
    @AfterEach
    void tearDown() {
        io.close();
        snapshot.restore();
    }

    private String write(String fileName, String... lines) throws IOException {
        Path file = tempDir.resolve(fileName);
        Files.write(file, List.of(lines), StandardCharsets.UTF_8);
        return file.toString();
    }

    // ------------------------------------------------------------------ loadClasses

    /**
     * Every column of a class row is applied to the matching ServantClass.
     */
    @Test
    void loadClassesAppliesEveryColumnToTheMatchingClass() throws IOException {
        String path = write("classes.csv", CLASSES_HEADER, "SABER,10,1.5,2.5,0.25");

        CsvReader.loadClasses(path);

        assertEquals(10.0, ServantClass.SABER.getSortOrder());
        assertEquals(1.5, ServantClass.SABER.getAttackMultiplier());
        assertEquals(2.5, ServantClass.SABER.getCriticalMultiplier());
        assertEquals(0.25, ServantClass.SABER.getCriticalRate());
    }

    /**
     * Several rows configure several classes.
     */
    @Test
    void loadClassesHandlesSeveralRows() throws IOException {
        String path = write("classes.csv", CLASSES_HEADER, "SABER,1,1.1,1.2,0.3", "CASTER,5,0.9,1.05,0.1");

        CsvReader.loadClasses(path);

        assertEquals(1.1, ServantClass.SABER.getAttackMultiplier());
        assertEquals(0.9, ServantClass.CASTER.getAttackMultiplier());
        assertEquals(0.1, ServantClass.CASTER.getCriticalRate());
    }

    /**
     * The first line (header) and the blank lines are skipped without any error.
     */
    @Test
    void loadClassesSkipsTheHeaderAndBlankLines() throws IOException {
        String path = write("classes.csv", CLASSES_HEADER, "", "   ", "LANCER,3,1.3,1.0,0.0");

        CsvReader.loadClasses(path);

        assertEquals(1.3, ServantClass.LANCER.getAttackMultiplier());
        assertEquals("", io.errors());
    }

    /**
     * An unknown class name is reported on System.err (with the name) and does not throw.
     */
    @Test
    void loadClassesReportsAnUnknownClass() throws IOException {
        String path = write("classes.csv", CLASSES_HEADER, "PALADIN,1,1.0,1.0,0.0");

        assertDoesNotThrow(() -> CsvReader.loadClasses(path));

        assertTrue(io.errors().contains("PALADIN"));
    }

    /**
     * A value that is not a number is reported as an invalid number format, and the class stays unchanged.
     */
    @Test
    void loadClassesReportsAnInvalidNumber() throws IOException {
        String path = write("classes.csv", CLASSES_HEADER, "SABER,abc,1.5,1.0,0.0");

        assertDoesNotThrow(() -> CsvReader.loadClasses(path));

        assertTrue(io.errors().contains("Invalid number format"));
        assertEquals(1.0, ServantClass.SABER.getSortOrder());
        assertEquals(1.0, ServantClass.SABER.getAttackMultiplier());
    }

    /**
     * A row with too few columns is reported as an invalid array index and does not throw.
     */
    @Test
    void loadClassesReportsMissingColumns() throws IOException {
        String path = write("classes.csv", CLASSES_HEADER, "SABER,1,1.5");

        assertDoesNotThrow(() -> CsvReader.loadClasses(path));

        assertTrue(io.errors().contains("Invalid array index"));
    }

    /**
     * A file that does not exist is reported ("Unable to read file") and does not throw.
     */
    @Test
    void loadClassesReportsAMissingFile() {
        String path = tempDir.resolve("does-not-exist.csv").toString();

        assertDoesNotThrow(() -> CsvReader.loadClasses(path));

        assertTrue(io.errors().contains("Unable to read file"));
    }

    /**
     * Rows read before a malformed row are kept (SABER is configured, then the bad row is reported).
     */
    @Test
    void loadClassesKeepsTheRowsReadBeforeAMalformedRow() throws IOException {
        String path = write("classes.csv", CLASSES_HEADER, "SABER,1,1.9,1.0,0.0", "PALADIN,2,1.0,1.0,0.0");

        CsvReader.loadClasses(path);

        assertEquals(1.9, ServantClass.SABER.getAttackMultiplier());
        assertFalse(io.errors().isEmpty());
    }

    // ------------------------------------------------------------------ loadServants

    /**
     * A servant row becomes a servant with the right identity and statistics.
     */
    @Test
    void loadServantsBuildsServantsFromTheRows() throws IOException {
        String path = write("servants.csv", SERVANTS_HEADER, "1,Artoria,SABER,1200,100,100,1.2,0.05");

        List<Servant> servants = CsvReader.loadServants(path);

        assertEquals(1, servants.size());
        Servant servant = servants.getFirst();
        assertEquals(1, servant.getId());
        assertEquals("Artoria", servant.getName());
        assertEquals(ServantClass.SABER, servant.getServantClass());
        assertEquals(1200, servant.getMaxHp());
        assertEquals(100, servant.getMaxMp());
        assertEquals(100, servant.getAtk());
        assertEquals(1.2, servant.getCriticalMultiplier());
        assertEquals(0.05, servant.getCriticalRate());
    }

    /**
     * The servants are build with their actions (3 basic + class skill + noble phantasm).
     */
    @Test
    void loadServantsReturnsFullyEquippedServants() throws IOException {
        String path = write("servants.csv", SERVANTS_HEADER, "1,Artoria,SABER,1200,100,100,1.2,0.05");

        Servant servant = CsvReader.loadServants(path).getFirst();

        assertEquals(5, servant.getAvailableActions().size());
        assertEquals("NP: Excalibur", servant.getAvailableActions().get(4).getName());
    }

    /**
     * The result is sorted by class order first, then by name inside a class.
     */
    @Test
    void loadServantsSortsByClassOrderThenByName() throws IOException {
        String path = write("servants.csv", SERVANTS_HEADER,
                "10,Gilgamesh,ARCHER,1000,100,100,1.0,0.0",
                "11,Melusine,LANCER,1000,100,100,1.0,0.0",
                "12,Muramasa,SABER,1000,100,100,1.0,0.0",
                "13,Ishtar,ARCHER,1000,100,100,1.0,0.0");

        List<Servant> servants = CsvReader.loadServants(path);

        assertEquals("Muramasa", servants.get(0).getName());
        assertEquals("Gilgamesh", servants.get(1).getName());
        assertEquals("Ishtar", servants.get(2).getName());
        assertEquals("Melusine", servants.get(3).getName());
    }

    /**
     * A file containing only the header gives an empty list.
     */
    @Test
    void loadServantsReturnsAnEmptyListForAHeaderOnlyFile() throws IOException {
        List<Servant> servants = CsvReader.loadServants(write("servants.csv", SERVANTS_HEADER));

        assertTrue(servants.isEmpty());
    }

    /**
     * A file that does not exist gives an empty list, reports the problem and does not throw.
     */
    @Test
    void loadServantsReturnsAnEmptyListForAMissingFile() {
        String path = tempDir.resolve("does-not-exist.csv").toString();

        List<Servant> servants = CsvReader.loadServants(path);

        assertNotNull(servants);
        assertTrue(servants.isEmpty());
        assertTrue(io.errors().contains("Unable to read file"));
    }

    /**
     * An unknown class is reported with its name; the row is not turned into a servant.
     */
    @Test
    void loadServantsReportsAnUnknownClass() throws IOException {
        String path = write("servants.csv", SERVANTS_HEADER, "1,Mash,PALADIN,1000,100,100,1.0,0.0");

        List<Servant> servants = CsvReader.loadServants(path);

        assertTrue(servants.isEmpty());
        assertTrue(io.errors().contains("PALADIN"));
    }

    /**
     * A number that is not a number is reported as an invalid number format.
     */
    @Test
    void loadServantsReportsAnInvalidNumber() throws IOException {
        String path = write("servants.csv", SERVANTS_HEADER, "1,Artoria,SABER,hmmm,100,100,1.0,0.0");

        List<Servant> servants = CsvReader.loadServants(path);

        assertTrue(servants.isEmpty());
        assertTrue(io.errors().contains("Invalid number format"));
    }

    /**
     * A row with missing columns is reported as an invalid array index.
     */
    @Test
    void loadServantsReportsMissingColumns() throws IOException {
        String path = write("servants.csv", SERVANTS_HEADER, "1,Artoria,SABER,1000");

        List<Servant> servants = CsvReader.loadServants(path);

        assertTrue(servants.isEmpty());
        assertTrue(io.errors().contains("Invalid array index"));
    }

    /**
     * Servants read before a malformed row are returned, and the error is reported.
     */
    @Test
    void loadServantsKeepsTheServantsReadBeforeAMalformedRow() throws IOException {
        String path = write("servants.csv", SERVANTS_HEADER,
                "1,Artoria,SABER,1000,100,100,1.0,0.0",
                "2,Mash,PALADIN,1000,100,100,1.0,0.0");

        List<Servant> servants = CsvReader.loadServants(path);

        assertFalse(servants.isEmpty());
        assertEquals("Artoria", servants.getFirst().getName());
        assertFalse(io.errors().isEmpty());
    }

    // ------------------------------------------------------------------ loadPatterns

    /**
     * A pattern row maps the servant id to its action types, in the same order as in the file.
     */
    @Test
    void loadPatternsMapsTheServantIdToItsActionTypes() throws IOException {
        String path = write("patterns.csv", PATTERNS_HEADER, "1,Attack,Defense,Break");

        Map<Integer, ActionType[]> patterns = CsvReader.loadPatterns(path);

        assertEquals(1, patterns.size());
        assertArrayEquals(new ActionType[]{ActionType.ATTACK, ActionType.DEFENSE, ActionType.BREAK}, patterns.get(1));
    }

    /**
     * Patterns can have different lengths: each servant keeps its own.
     */
    @Test
    void loadPatternsSupportsPatternsOfDifferentLengths() throws IOException {
        String path = write("patterns.csv", PATTERNS_HEADER,
                "1,Attack",
                "2,Attack,Defense,Break,Skill,NP,Attack,Defense,Break");

        Map<Integer, ActionType[]> patterns = CsvReader.loadPatterns(path);

        assertEquals(2, patterns.size());
        assertEquals(1, patterns.get(1).length);
        assertEquals(8, patterns.get(2).length);
    }

    /**
     * An unknown action is reported with its name and the whole row is rejected.
     */
    @Test
    void loadPatternsReportsAnUnknownAction() throws IOException {
        String path = write("patterns.csv", PATTERNS_HEADER, "1,Attack,PlayEveryTypeMoonGameAndVisualNovelWatchEveryAnimeAndReadEveryMangaAndLightNovel");

        Map<Integer, ActionType[]> patterns = CsvReader.loadPatterns(path);

        assertTrue(io.errors().contains("PlayEveryTypeMoonGameAndVisualNovelWatchEveryAnimeAndReadEveryMangaAndLightNovel"));
        assertFalse(patterns.containsKey(1));
    }

    /**
     * A row with no action is reported as an unavailable pattern and rejected.
     */
    @Test
    void loadPatternsReportsAnEmptyPattern() throws IOException {
        String path = write("patterns.csv", PATTERNS_HEADER, "1");

        Map<Integer, ActionType[]> patterns = CsvReader.loadPatterns(path);

        assertTrue(io.errors().contains("Unavailable pattern"));
        assertTrue(patterns.isEmpty());
    }

    /**
     * An id that is not a number is reported as an invalid number format.
     */
    @Test
    void loadPatternsReportsAnInvalidId() throws IOException {
        String path = write("patterns.csv", PATTERNS_HEADER, "abc,Attack");

        Map<Integer, ActionType[]> patterns = CsvReader.loadPatterns(path);

        assertTrue(io.errors().contains("Invalid number format"));
        assertTrue(patterns.isEmpty());
    }

    /**
     * A file that does not exist gives an empty map, reports the problem and does not throw.
     */
    @Test
    void loadPatternsReturnsAnEmptyMapForAMissingFile() {
        String path = tempDir.resolve("does-not-exist.csv").toString();

        Map<Integer, ActionType[]> patterns = CsvReader.loadPatterns(path);

        assertNotNull(patterns);
        assertTrue(patterns.isEmpty());
        assertTrue(io.errors().contains("Unable to read file"));
    }

    /**
     * Patterns read before a malformed row are returned, and the error is reported.
     */
    @Test
    void loadPatternsKeepsThePatternsReadBeforeAMalformedRow() throws IOException {
        String path = write("patterns.csv", PATTERNS_HEADER, "1,Attack", "2,azertyuioop");

        Map<Integer, ActionType[]> patterns = CsvReader.loadPatterns(path);

        assertTrue(patterns.containsKey(1));
        assertFalse(patterns.containsKey(2));
        assertFalse(io.errors().isEmpty());
    }
}
