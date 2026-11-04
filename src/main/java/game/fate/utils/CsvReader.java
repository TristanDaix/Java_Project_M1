package game.fate.utils;

import game.fate.exceptions.UnknowActionException;
import game.fate.exceptions.UnknowServantClassException;
import game.fate.models.actions.ActionType;
import game.fate.models.servant.Servant;
import game.fate.models.servant.ServantClass;
import game.fate.services.factories.ServantFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * Utility responsible for deserializing external game data files into active Java objects.
 */
public class CsvReader {

    /**
     * Reads a formatted CSV file and set the data for the different servant class.
     * Catches structural exceptions to prevent crashes on malformed data.
     *
     * @param filePath The local system path resolving to the target CSV document.
     */
    public static void loadClasses(String filePath) {
        Path path = Path.of(filePath);
        try {
            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i);
                if (line.isBlank()) continue;

                // TODO: separate
                String[] fields = line.split(",");
                String className = fields[0].trim().toUpperCase();
                int sortOrder = Integer.parseInt(fields[1].trim());
                double atkMultiplier = Double.parseDouble(fields[2].trim());
                double criticalMultiplier = Double.parseDouble(fields[3].trim());
                double criticalRate = Double.parseDouble(fields[4].trim());

                try {
                    ServantClass servantClass = ServantClass.valueOf(className);
                    servantClass.setStats(sortOrder, atkMultiplier, criticalMultiplier, criticalRate);
                } catch (IllegalArgumentException e) {
                    throw new UnknowServantClassException(className);
                }
            }
        } catch (IOException e) {
            System.err.println("Error: Unable to read file " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Error: Invalid number format " + e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Error: Invalid array index " + e.getMessage());
        } catch (UnknowServantClassException e) {
            System.err.println("Error: Unknow servant class " + e.getMessage());
        }
    }

    /**
     * Reads a formatted CSV file and constructs a master list of Servant entities.
     * Catches structural exceptions to prevent crashes on malformed data, and returns
     * the successfully parsed list sorted primarily by class order and secondarily by name.
     *
     * @param filePath The local system path resolving to the target CSV document.
     * @return An ordered list of successfully instantiated Servants.
     */
    public static List<Servant> loadServants(String filePath) {
        List<Servant> servants = new ArrayList<>();
        Path path = Path.of(filePath);

        try {
            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);

            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i);

                if (line.isBlank()) continue;

                servants.add(parseServantFromLine(line));
            }
        } catch (IOException e) {
            System.err.println("Error: Unable to read file " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Error: Invalid number format " + e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Error: Invalid array index " + e.getMessage());
        } catch (UnknowServantClassException e) {
            System.err.println("Error: Unknow servant class " + e.getMessage());
        }

        return servants.stream()
                .sorted(Comparator.comparing((Servant s) -> s.getServantClass().getSortOrder()).thenComparing(Servant::getName))
                .toList();
    }

    /**
     * Parses a single row of comma-separated text into discrete parameters and delegates
     * instantiation to the ServantFactory.
     *
     * @param line A single plaintext row representing one Servant's data.
     * @return A constructed Servant object.
     * @throws NumberFormatException          If numerical columns contain invalid text.
     * @throws ArrayIndexOutOfBoundsException If the line lacks the expected number of column fields.
     * @throws UnknowServantClassException    If the designated class column fails to match a known ServantClass enumeration.
     */
    private static Servant parseServantFromLine(String line) throws NumberFormatException, ArrayIndexOutOfBoundsException, UnknowServantClassException {
        String[] fields = line.split(",");

        int id = Integer.parseInt(fields[0].trim());
        String name = fields[1].trim();
        String className = fields[2].trim();
        int maxHp = Integer.parseInt(fields[3].trim());
        int maxMP = Integer.parseInt(fields[4].trim());
        int atk = Integer.parseInt(fields[5].trim());
        double criticalMultiplier = Double.parseDouble(fields[6].trim());
        double criticalRate = Double.parseDouble(fields[7].trim());

        ServantClass servantClass;
        try {
            servantClass = ServantClass.valueOf(className.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new UnknowServantClassException(className);
        }

        // Delegate object construction to ServantFactory
        return ServantFactory.createServant(id, name, servantClass, maxHp, maxMP, atk, criticalMultiplier, criticalRate);
    }

    /**
     * Reads a formatted CSV file and constructs a mapping of Servant id to their combat patterns.
     *
     * @param filePath The local system path resolving to the target CSV document.
     * @return A map linking a ServantId to its sequential array of action names.
     */
    public static Map<Integer, ActionType[]> loadPatterns(String filePath) {
        Map<Integer, ActionType[]> patterns = new HashMap<>();
        Path path = Path.of(filePath);

        try {
            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);

            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i);
                if (line.isBlank()) continue;

                // TODO: separate
                String[] fields = line.split(",");
                int id = Integer.parseInt(fields[0].trim());

                ActionType[] pattern = new ActionType[fields.length - 1];
                for (int j = 1; j < fields.length; j++) {
                    String actionType = fields[j].trim();

                    try {
                        pattern[j - 1] = ActionType.valueOf(actionType.toUpperCase());
                    } catch (IllegalArgumentException e) {
                        throw new UnknowActionException(actionType);
                    }
                }

                patterns.put(id, pattern);
            }
        } catch (IOException e) {
            System.err.println("Error: Unable to read file " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Error: Invalid number format " + e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Error: Invalid array index " + e.getMessage());
        } catch (UnknowActionException e) {
            System.err.println("Error: Unknow action " + e.getMessage());
        }

        return patterns;
    }
}
