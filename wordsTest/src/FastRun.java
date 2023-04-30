import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class FastRun {
    private ArrayList<String> words;
    private ArrayList<List<String>> allMatches;

    public FastRun() {
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

    public void findMatches() {
        List<BitSet> bitSets = new ArrayList<>();
        for (String word : words) {
            bitSets.add(getBitSet(word));
        }

        findMatchesRecursive(bitSets, new BitSet(26), 0, 0);
    }

    private void findMatchesRecursive(List<BitSet> bitSets, BitSet currentBitSet, int startIndex, int depth) {
        if (depth == 5) {
            List<String> match = new ArrayList<>();
            for (int i = 0; i < 26; i++) {
                if (currentBitSet.get(i)) {
                    match.add(words.get(i));
                }
            }
            allMatches.add(match);
            return;
        }

        for (int i = startIndex; i < bitSets.size(); i++) {
            BitSet newBitSet = (BitSet) currentBitSet.clone();
            if (!newBitSet.intersects(bitSets.get(i))) {
                newBitSet.or(bitSets.get(i));
                findMatchesRecursive(bitSets, newBitSet, i + 1, depth + 1);
            }
        }
    }

    private BitSet getBitSet(String word) {
        BitSet bitSet = new BitSet(26);
        for (char ch : word.toCharArray()) {
            if (ch >= 'a' && ch <= 'z') {
                bitSet.set(ch - 'a');
            }
        }
        return bitSet;
    }

    public void printMatches() {
        System.out.println("All combinations of 5 words with no shared letters: ");
        for (List<String> match : allMatches) {
            System.out.println(match);
        }
    }

    public static void main(String[] args) {
        FastRun program = new FastRun();
        program.readWordsFromFile("/home/jaro/Downloads/woerter.txt");

        long startTime = System.currentTimeMillis();
        program.findMatches();
        long endTime = System.currentTimeMillis();
        program.printMatches();

        System.out.println("Elapsed time: " + (endTime - startTime) + " milliseconds");
    }
}
