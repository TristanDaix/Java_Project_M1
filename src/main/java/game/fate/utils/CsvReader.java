package game.fate.utils;

import game.fate.exceptions.UnknowServantClassException;
import game.fate.models.Servant;
import game.fate.models.ServantClass;
import game.fate.models.servantClass.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Class responsible for reading the different csv file for the application data
 */
public class CsvReader {

    /**
     * Read a csv file to load a list of servant
     *
     * @param filePath The path to the csv file
     * @return The list of servant loaded
     */
    public static List<Servant> loadServants(String filePath) {
        List<Servant> servants = new ArrayList<>();
        Path path = Path.of(filePath);

        try {
            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);

            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i);

                if (line.isBlank()) continue;

                servants.add(getServant(line));
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
     * Create the servant from a line of csv
     *
     * @param line The servant's data line
     * @return The servant object
     * @throws NumberFormatException          Error in number format
     * @throws ArrayIndexOutOfBoundsException Error in number of arguments
     * @throws UnknowServantClassException    Error in servant class
     */
    private static Servant getServant(String line) throws NumberFormatException, ArrayIndexOutOfBoundsException, UnknowServantClassException {
        String[] fields = line.split(",");

        int id = Integer.parseInt(fields[0].trim());
        String name = fields[1].trim();
        String servantClass = fields[2].trim();
        int maxHp = Integer.parseInt(fields[3].trim());
        int atk = Integer.parseInt(fields[4].trim());
        double criticalMultiplier = Double.parseDouble(fields[5].trim());
        double criticalRate = Double.parseDouble(fields[6].trim());

        return switch (servantClass.toLowerCase()) {
            case "saber" -> new Saber(id, name, ServantClass.SABER, maxHp, atk, criticalMultiplier, criticalRate);
            case "archer" -> new Archer(id, name, ServantClass.ARCHER, maxHp, atk, criticalMultiplier, criticalRate);
            case "lancer" -> new Lancer(id, name, ServantClass.LANCER, maxHp, atk, criticalMultiplier, criticalRate);
            case "rider" -> new Rider(id, name, ServantClass.RIDER, maxHp, atk, criticalMultiplier, criticalRate);
            case "caster" -> new Caster(id, name, ServantClass.CASTER, maxHp, atk, criticalMultiplier, criticalRate);
            case "assassin" ->
                    new Assassin(id, name, ServantClass.ASSASSIN, maxHp, atk, criticalMultiplier, criticalRate);
            case "berserker" ->
                    new Berserker(id, name, ServantClass.BERSERKER, maxHp, atk, criticalMultiplier, criticalRate);
            case "ruler" -> new Ruler(id, name, ServantClass.RULER, maxHp, atk, criticalMultiplier, criticalRate);
            case "avenger" -> new Avenger(id, name, ServantClass.AVENGER, maxHp, atk, criticalMultiplier, criticalRate);
            case "mooncancer" ->
                    new MoonCancer(id, name, ServantClass.MOONCANCER, maxHp, atk, criticalMultiplier, criticalRate);
            case "alterego" ->
                    new AlterEgo(id, name, ServantClass.ALTEREGO, maxHp, atk, criticalMultiplier, criticalRate);
            case "foreigner" ->
                    new Foreigner(id, name, ServantClass.FOREIGNER, maxHp, atk, criticalMultiplier, criticalRate);
            case "pretender" ->
                    new Pretender(id, name, ServantClass.PRETENDER, maxHp, atk, criticalMultiplier, criticalRate);
            case "shielder" ->
                    new Shielder(id, name, ServantClass.SHIELDER, maxHp, atk, criticalMultiplier, criticalRate);
            default -> throw new UnknowServantClassException(servantClass);
        };
    }
}
