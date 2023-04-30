import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class GraphRun {
    private ArrayList<String> words;
    private ArrayList<List<String>> allMatches;
    private ArrayList<BitSet> adjacencyMatrix;

    public GraphRun() {
        words = new ArrayList<>();
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

    public void buildGraph() {
        int n = words.size();
        adjacencyMatrix = new ArrayList<>(n);

        for (int i = 0; i < n; i++) {
            BitSet row = new BitSet(n);
            String word1 = words.get(i);
            for (int j = i + 1; j < n; j++) {
                String word2 = words.get(j);
                if (!shareLetters(word1, word2)) {
                    row.set(j);
                }
            }
            adjacencyMatrix.add(row);
        }
    }

    public boolean shareLetters(String word1, String word2) {
        BitSet letters = new BitSet(26);
        for (char ch : word1.toCharArray()) {
            if (ch >= 'a' && ch <= 'z') {
                letters.set(ch - 'a');
            }
        }

        for (char ch : word2.toCharArray()) {
            if (ch >= 'a' && ch <= 'z' && letters.get(ch - 'a')) {
                return true;
            }
        }
        return false;
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

        for (int i = startIndex; i < words.size(); i++) {
            boolean isConnectedToClique = true;
            for (int node : clique) {
                if (!adjacencyMatrix.get(node).get(i)) {
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
        GraphRun program = new GraphRun();
        program.readWordsFromFile("C:\\Users\\Clay\\git\\projects\\wordsTest\\src\\woerter.txt");

        long startTime = System.currentTimeMillis();
        program.buildGraph();
        program.findMatches();
        long endTime = System.currentTimeMillis();
        program.printMatches();

        System.out.println("Elapsed time: " + (endTime - startTime) + " milliseconds");
    }

}
