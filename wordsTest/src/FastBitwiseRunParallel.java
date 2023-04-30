//393620 milliseconds

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.stream.Collectors;

public class FastBitwiseRunParallel {
    private String[] words;
    private int[][] wordFrequencies;

    public FastBitwiseRunParallel() {
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

    public int findMatches() {
        ForkJoinPool pool = new ForkJoinPool();
        return pool.invoke(new MatchFinderTask(0, 0, new int[5]));
    }

    private boolean sharesCommonLetter(int[] freq1, int[] freq2) {
        for (int i = 0; i < 26; i++) {
            if (freq1[i] > 0 && freq2[i] > 0) {
                return true;
            }
        }
        return false;
    }

    private class MatchFinderTask extends RecursiveTask<Integer> {
        private int startIndex;
        private int cliqueSize;
        private int[] clique;

        public MatchFinderTask(int startIndex, int cliqueSize, int[] clique) {
            this.startIndex = startIndex;
            this.cliqueSize = cliqueSize;
            this.clique = clique.clone();
        }

        @Override
        protected Integer compute() {
            if (cliqueSize == 5) {
                return 1;
            }

            if (wordFrequencies.length - startIndex < 5 - cliqueSize) {
                return 0;
            }

            int count = 0;
            List<MatchFinderTask> subtasks = new ArrayList<>();

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
                    MatchFinderTask subtask = new MatchFinderTask(i + 1, cliqueSize + 1, clique);
                    subtask.fork();
                    subtasks.add(subtask);
                }
            }

            for (MatchFinderTask subtask : subtasks) {
                count += subtask.join();
            }

            return count;
        }
    }

    public static void main(String[] args) {
        FastBitwiseRunParallel program = new FastBitwiseRunParallel();
        program.readWordsAndCreateFrequenciesFromFile("C:\\Users\\Clay\\git\\projects\\wordsTest\\src\\woerter.txt");
        long startTime = System.currentTimeMillis();
        int matchCount = program.findMatches();
        long endTime = System.currentTimeMillis();

        System.out.println("Number of combinations of 5 words with no shared letters: " + matchCount);
        System.out.println("Elapsed time: " + (endTime - startTime) + " milliseconds");
    }
}
