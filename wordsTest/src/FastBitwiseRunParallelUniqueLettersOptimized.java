//
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.stream.Collectors;

public class FastBitwiseRunParallelUniqueLettersOptimized {
    private List<String> words;
    private List<BitSet> wordMasks;
    private final int threshold = 2;

    public FastBitwiseRunParallelUniqueLettersOptimized() {
        words = new ArrayList<>();
        wordMasks = new ArrayList<>();
    }

    public void readWordsAndCreateMasksFromFile(String filename) {
        File file = new File(filename);
        List<String> lines = readLinesFromFile(file);
        for (String word : lines) {
            BitSet mask = createMask(word);
            if (mask != null) {
                words.add(word);
                wordMasks.add(mask);
            }
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

    private BitSet createMask(String word) {
        BitSet mask = new BitSet(26);
        for (char ch : word.toCharArray()) {
            if (ch >= 'a' && ch <= 'z') {
                int index = ch - 'a';
                if (mask.get(index)) {
                    return null;
                }
                mask.set(index);
            }
        }
        return mask;
    }

    public int findMatches() {
        ForkJoinPool pool = new ForkJoinPool();
        return pool.invoke(new MatchFinderTask(0, 0, new BitSet(26)));
    }

    private class MatchFinderTask extends RecursiveTask<Integer> {
        private int startIndex;
        private int cliqueSize;
        private BitSet combinedMask;

        public MatchFinderTask(int startIndex, int cliqueSize, BitSet combinedMask) {
            this.startIndex = startIndex;
            this.cliqueSize = cliqueSize;
            this.combinedMask = combinedMask;
        }

        @Override
        protected Integer compute() {
            if (cliqueSize == 5) {
                return 1;
            }

            if (wordMasks.size() - startIndex < 5 - cliqueSize) {
                return 0;
            }

            int count = 0;

            if (cliqueSize < threshold) {
                List<MatchFinderTask> subtasks = new ArrayList<>();

                for (int i = startIndex; i < wordMasks.size(); i++) {
                    BitSet mask = wordMasks.get(i);
                    if (!combinedMask.intersects(mask)) {
                        BitSet newCombinedMask = (BitSet) combinedMask.clone();
                        newCombinedMask.or(mask);
                        MatchFinderTask subtask = new MatchFinderTask(i + 1, cliqueSize + 1, newCombinedMask);
                        subtask.fork();
                        subtasks.add(subtask);
                    }
                }

                for (MatchFinderTask subtask : subtasks) {
                    count += subtask.join();
                }
            } else {
                for (int i = startIndex; i < wordMasks.size(); i++) {
                    BitSet mask = wordMasks.get(i);
                    if (!combinedMask.intersects(mask)) {
                        BitSet newCombinedMask = (BitSet) combinedMask.clone();
                        newCombinedMask.or(mask);
                        count += new MatchFinderTask(i + 1, cliqueSize + 1, newCombinedMask).compute();
                    }
                }
            }

            return count;
        }
    }

    public static void main(String[] args) {
        FastBitwiseRunParallelUniqueLettersOptimized program = new FastBitwiseRunParallelUniqueLettersOptimized();
        String currentDirectory = new File("").getAbsolutePath();
        program.readWordsAndCreateMasksFromFile(currentDirectory +"\\src\\woerter.txt");

        long startTime = System.currentTimeMillis();
        int matchCount = program.findMatches();
        long endTime = System.currentTimeMillis();

        System.out.println("Number of combinations of 5 words with no shared letters and no repeated letters: " + matchCount);
        System.out.println("Elapsed time: " + (endTime - startTime) + " milliseconds");
    }

}


