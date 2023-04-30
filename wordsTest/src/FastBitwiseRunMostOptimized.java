//
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class FastBitwiseRunMostOptimized {
    private String[] words;
    private int[][] wordFrequencies;
    private int matchCount;

    public FastBitwiseRunMostOptimized() {
        matchCount = 0;
    }

    public void readWordsAndCreateFrequenciesFromFile(String filename) {
        File file = new File(filename);
        List<String> lines = readLinesFromFile(file);
        words = new String[lines.size()];
        wordFrequencies = new int[lines.size()][26];

        for (int i = 0; i < lines.size(); i++) {
            String word = lines.get(i);
            words[i] = word;
            wordFrequencies[i] = createFrequencies(word);
        }
    }

    private List<String> readLinesFromFile(File file) {
        try {
            return Files.lines(Path.of(file.toURI())).collect(Collectors.toList());
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            return List.of();
        }
    }

    private int[] createFrequencies(String word) {
        int[] freq = new int[26];
        for (char ch : word.toCharArray()) {
            if (ch >= 'a' && ch <= 'z') {
                freq[ch - 'a']++;
            }
        }
        return freq;
    }

    public void findMatches() {
        findMatchesRecursive(new int[5], 0, 0);
    }

    private void findMatchesRecursive(int[] clique, int cliqueSize, int startIndex) {
        if (cliqueSize == 5) {
            matchCount++;
            return;
        }

        if (wordFrequencies.length - startIndex < 5 - cliqueSize) {
            return;
        }

        for (int i = startIndex; i < wordFrequencies.length; i++) {
            boolean isConnectedToClique = true;
            for (int j = 0; j < cliqueSize; j++) {
                if (sharesCommonLetter(wordFrequencies[clique[j]], wordFrequencies[i])) {
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

    private boolean sharesCommonLetter(int[] freq1, int[] freq2) {
        for (int i = 0; i < 26; i++) {
            if (freq1[i] > 0 && freq2[i] > 0) {
                return true;
            }
        }
        return false;
    }

    public void printMatches() {
        System.out.println("Number of combinations of 5 words with no shared letters: " + matchCount);
    }

    public static void main(String[] args) {
        FastBitwiseRunMostOptimized program = new FastBitwiseRunMostOptimized();
        program.readWordsAndCreateFrequenciesFromFile("C:\\Users\\Clay\\git\\projects\\wordsTest\\src\\woerter.txt");

        long startTime = System.currentTimeMillis();
        program.findMatches();
        long endTime = System.currentTimeMillis();
        program.printMatches();
        System.out.println("Elapsed time: " + (endTime - startTime) + " milliseconds");
    }
}