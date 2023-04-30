import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FilterStringsInFile {
    public static void main(String[] args) {
        File file = new File("C:\\Users\\Clay\\git\\projects\\wordsTest\\src\\woerter.txt");
        filterFiveLetterStrings(file);
    }

    public static void filterFiveLetterStrings(File file) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] words = line.split("\\s+");
                for (String word : words) {
                    String cleanedWord = word.replaceAll("[^a-zA-Z]", "").toLowerCase();
                    if (cleanedWord.length() == 5) {
                        lines.add(cleanedWord);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing file: " + e.getMessage());
        }
    }
}
