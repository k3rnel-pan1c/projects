import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class NumberFilter {
    public static void main(String[] args) {
        long maxValue = 1_000_000_000_000_000L;
        String fileName = "filtered_numbers.txt";
        LocalDateTime startTime = LocalDateTime.now();

        try (FileWriter writer = new FileWriter(fileName)) {
            for (long number = 1; number < maxValue; number++) {
                if (isAscendingOrder(number) && !containsDigits(number, '0', '1', '5') && countOccurrences(number, '3') <= 1) {
                    writer.write(Long.toString(number));
                    writer.write(System.lineSeparator());
                }

                LocalDateTime currentTime = LocalDateTime.now();
                long minutesElapsed = java.time.Duration.between(startTime, currentTime).toMinutes();

                if (minutesElapsed >= 1) {
                    System.out.println("Current number: " + number);
                    startTime = currentTime;
                }
            }

            System.out.println("Numbers have been written to " + fileName);
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
            e.printStackTrace();
        }
    }

    private static boolean isAscendingOrder(long number) {
        String digits = String.valueOf(number);
        int length = digits.length();

        for (int i = 0; i < length - 1; i++) {
            if (digits.charAt(i) > digits.charAt(i + 1)) {
                return false;
            }
        }

        return true;
    }

    private static boolean containsDigits(long number, char... digits) {
        String numberString = String.valueOf(number);
        for (char digit : digits) {
            if (numberString.indexOf(digit) != -1) {
                return true;
            }
        }
        return false;
    }

    private static int countOccurrences(long number, char digit) {
        return String.valueOf(number).length() - String.valueOf(number).replace(Character.toString(digit), "").length();
    }
}
