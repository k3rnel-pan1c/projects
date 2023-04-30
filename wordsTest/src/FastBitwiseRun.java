import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FastBitwiseRun {
    private ArrayList<String> words;
    private ArrayList<Integer> wordMasks;
    private ArrayList<List<String>> allMatches;

    public FastBitwiseRun() {
        words = new ArrayList<>();
        wordMasks = new ArrayList<>();
        allMatches = new ArrayList<>();
    }

    public void readWordsFromFile(String filename) {
        File file = new File(filename);
        words = (ArrayList<String>) readLinesFromFile(file);
    }

    private List<String> readLinesFromFile(File file) {
        List<String> lines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }

        return lines;
    }

    public void createWordMasks() {
        for (String word : words) {
            wordMasks.add(createMask(word));
        }
    }

    private int createMask(String word) {
        int mask = 0;
        for (char ch : word.toCharArray()) {
            if (ch >= 'a' && ch <= 'z') {
                mask |= (1 << (ch - 'a'));
            }
        }
        return mask;
    }

    public void findMatches() {
        findMatchesRecursive(new ArrayList<>(), 0);
    }

    private void findMatchesRecursive(ArrayList<Integer> clique, int startIndex) {
        if (clique.size() == 5) {
            List<String> match = new ArrayList<>();
            for (int index : clique) {
                match.add(words.get(index));
            }
            allMatches.add(match);
            return;
        }

        // Pruning: stop searching if there are not enough words left to form a 5-word clique
        if (wordMasks.size() - startIndex < 5 - clique.size()) {
            return;
        }

        for (int i = startIndex; i < wordMasks.size(); i++) {
            boolean isConnectedToClique = true;
            for (int node : clique) {
                if ((wordMasks.get(node) & wordMasks.get(i)) != 0) {
                    isConnectedToClique = false;
                    break;
                }
            }

            if (isConnectedToClique) {
                clique.add(i);
                findMatchesRecursive(clique, i + 1);
                clique.remove(clique.size() - 1);
            }
        }
    }

    public void printMatches() {
        System.out.println("All combinations of 5 words with no shared letters: ");
        for (List<String> match : allMatches) {
            System.out.println(match);
        }
    }

    public static void main(String[] args) {
        FastBitwiseRun program = new FastBitwiseRun();
        program.readWordsFromFile("/home/jaro/Downloads/woerter.txt");

        long startTime = System.currentTimeMillis();
        program.createWordMasks();
        program.findMatches();
        long endTime = System.currentTimeMillis();
        program.printMatches();
        System.out.println("Elapsed time: " + (endTime - startTime) + " milliseconds");
    }
}