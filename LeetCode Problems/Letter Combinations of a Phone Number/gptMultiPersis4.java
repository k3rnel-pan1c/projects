import java.util.HashSet;
import java.util.Set;
public class gptMultiPersis4 {
    public static void main(String[] args) {
        int persistence = 9; // Set the desired multiplication persistence

        if (persistence < 1) {
            System.out.println("Persistence should be a positive integer.");
            return;
        }

        long smallestNumber = findSmallestNumberWithPersistence(persistence);
        System.out.println("The smallest number with a multiplication persistence of " + persistence + " is: " + smallestNumber);
    }

    public static long findSmallestNumberWithPersistence(int persistence) {
        int numDigits = persistence + 1; // Number of digits needed to reach the desired persistence
        long number = (long) Math.pow(10, numDigits - 1); // Start with the smallest number of the required digits
        Set<Long> checkedNumbers = new HashSet<>();

        while (true) {
            if (checkedNumbers.contains(getSortedDigits(number))) {
                number++;
                continue;
            }

            checkedNumbers.add(getSortedDigits(number));

            if (containsZeroOrFive(number) || containsTwoThrees(number)) {
                number++;
                continue;
            }

            if (getPersistence(number) >= persistence) {
                return number;
            }

            number++;
        }
    }

    public static long getSortedDigits(long number) {
        int[] digits = new int[10]; // Array to hold counts of each digit (0-9)
        while (number != 0) {
            int digit = (int) (number % 10);
            digits[digit]++;
            number /= 10;
        }

        long sortedNumber = 0;
        for (int i = 9; i >= 0; i--) {
            for (int j = 0; j < digits[i]; j++) {
                sortedNumber = sortedNumber * 10 + i;
            }
        }

        return sortedNumber;
    }

    public static int getPersistence(long number) {
        int persistence = 0;
        while (number >= 10) {
            long result = 1;
            while (number != 0) {
                result *= number % 10;
                number /= 10;
            }
            number = result;
            persistence++;
        }
        return persistence;
    }

    public static boolean containsZeroOrFive(long number) {
        while (number != 0) {
            long digit = number % 10;
            if (digit == 0 || digit == 5) {
                return true;
            }
            number /= 10;
        }
        return false;
    }

    public static boolean containsTwoThrees(long number) {
        int count = 0;
        while (number != 0) {
            long digit = number % 10;
            if (digit == 3) {
                count++;
                if (count >= 2) {
                    return true;
                }
            }
            number /= 10;
        }
        return false;
    }

    public static boolean isAscendingOrder(long number) {
        long prevDigit = 9; // Initialize with highest possible digit
        while (number != 0) {
            long digit = number % 10;
            if (digit > prevDigit) {
                return false;
            }
            prevDigit = digit;
            number /= 10;
        }
        return true;
    }
}
