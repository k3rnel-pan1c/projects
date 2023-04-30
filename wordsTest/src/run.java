import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.BitSet;

public class run {
    private ArrayList<String> woerter;
    private ArrayList<List<String>> allMatches;
    private volatile boolean isComputing = false;
    Scanner readInput = new Scanner(System.in);

    public void menu() {
        System.out.println("\n" + "Menu\n"
                + "Wählen sie eine der folgenden Optionen(Alle Wahlen geschehen über die Zahlen vor den Optionen):\n"
                + "1. Wörter einlesen\n" + "2. Matches finden \n" + "3. Wörter anzeigen");
        String inputString = readInput.next();
        try {
            int input = Integer.parseInt(inputString);
            if (input == 1) {
                TextFileToArray();
            }
            if (input == 2) {
                allMatches = new ArrayList<>();
                isComputing = true;
                Thread timerThread = new Thread(() -> {
                    long startTime = System.currentTimeMillis();
                    while (isComputing) {
                        long currentTime = System.currentTimeMillis();
                        System.out.println("Elapsed time: " + (currentTime - startTime) + " milliseconds");
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
                timerThread.start();
                matchWords(woerter, new ArrayList<>(), 0, 0);
                isComputing = false;
                System.out.println("All combinations of 5 words with no shared letters: ");
                for (List<String> match : allMatches) {
                    System.out.println(match);
                }
            }
            if (input == 3) {
                for (String s : woerter) {
                    System.out.println(s);
                }
                menu();
            }
        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean matchWords(List<String> words, List<String> result, int startIndex, int depth) {
        if (depth == 5) {
            allMatches.add(new ArrayList<>(result));
            return false;
        }

        int availableWords = words.size() - startIndex;
        if (availableWords < 5 - depth) {
            return false;
        }

        for (int i = startIndex; i < words.size(); i++) {
            String word = words.get(i);
            if (!shareLetters(result, word)) {
                result.add(word);
                matchWords(words, result, i + 1, depth + 1);
                result.remove(result.size() - 1);
            }
        }

        return false;
    }

    public static boolean shareLetters(List<String> words, String newWord) {
        BitSet letterBits = new BitSet(26);

        for (char ch : newWord.toCharArray()) {
            if (ch >= 'a' && ch <= 'z') {
                letterBits.set(ch - 'a');
            }
        }

        for (String word : words) {
            BitSet wordBits = new BitSet(26);
            for (char ch : word.toCharArray()) {
                if (ch >= 'a' && ch <= 'z') {
                    wordBits.set(ch - 'a');
                }
            }
            if (wordBits.intersects(letterBits)) {
                return true;
            }
        }

        return false;
    }

    private void TextFileToArray() {
        File file = new File("/home/jaro/Downloads/woerter.txt");
        woerter = (ArrayList<String>) readLinesFromFile(file);
        woerter = (ArrayList<String>) sortWordsByFrequency(woerter);
        System.out.println("Success!");
        menu();
    }

    private List<String> sortWordsByFrequency(List<String> words) {
        Map<String, Integer> frequencyMap = new HashMap<>();
        for (String word : words) {
            frequencyMap.put(word, frequencyMap.getOrDefault(word, 0) + 1);
        }

        Collections.sort(words, new Comparator<String>() {
            @Override
            public int compare(String word1, String word2) {
                int freq1 = frequencyMap.get(word1);
                int freq2 = frequencyMap.get(word2);
                if (freq1 == freq2) {
                    return word1.compareTo(word2);
                }
                return Integer.compare(freq2, freq1);
            }
        });

        return words;
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
    public static void main(String[] args) {
        run program = new run();
        program.menu();
    }
    }

