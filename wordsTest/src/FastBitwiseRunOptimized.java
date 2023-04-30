//584525 milliseconds
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class FastBitwiseRunOptimized {
    private ArrayList<String> words;
    private BitSet[] wordMasks;
    private ArrayList<int[]> allMatches;

    public FastBitwiseRunOptimized() {
        words = new ArrayList<>();
        allMatches = new ArrayList<>();
    }

    public void readWordsAndCreateMasksFromFile(String filename) {
        File file = new File(filename);
        List<String> lines = readLinesFromFile(file);
        words = new ArrayList<>(lines.size());
        wordMasks = new BitSet[lines.size()];

        for (int i = 0; i < lines.size(); i++) {
            String word = lines.get(i);
            words.add(word);
            wordMasks[i] = createMask(word);
        }
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

    private BitSet createMask(String word) {
        BitSet mask = new BitSet(26);
        for (char ch : word.toCharArray()) {
            if (ch >= 'a' && ch <= 'z') {
                mask.set(ch - 'a');
            }
        }
        return mask;
    }

    public void findMatches() {
        findMatchesRecursive(new int[5], 0, 0);
    }

    private void findMatchesRecursive(int[] clique, int cliqueSize, int startIndex) {
        if (cliqueSize == 5) {
            allMatches.add(clique.clone());
            return;
        }

        if (wordMasks.length - startIndex < 5 - cliqueSize) {
            return;
        }

        for (int i = startIndex; i < wordMasks.length; i++) {
            boolean isConnectedToClique = true;
            for (int j = 0; j < cliqueSize; j++) {
                if (wordMasks[clique[j]].intersects(wordMasks[i])) {
                    isConnectedToClique = false;
                    break;
                }
            }

            if (isConnectedToClique) {
                clique[cliqueSize] = i;
                findMatchesRecursive(clique, cliqueSize + 1, i + 1);
            }
        }
    }

    public void printMatches() {
        System.out.println("All combinations of 5 words with no shared letters: ");
        for (int[] match : allMatches) {
            for (int index : match) {
                System.out.print(words.get(index) + " ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        FastBitwiseRunOptimized program = new FastBitwiseRunOptimized();
        program.readWordsAndCreateMasksFromFile("C:\\Users\\Clay\\git\\projects\\wordsTest\\src\\woerter.txt");

        long startTime = System.currentTimeMillis();
        program.findMatches();
        long endTime = System.currentTimeMillis();
        program.printMatches();
        System.out.println("Elapsed time: " + (endTime - startTime) + " milliseconds");
    }
}
