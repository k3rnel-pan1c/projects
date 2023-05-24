public class gptMultiPersis2 {
    public static void main(String[] args) {
        int persistence = 11; // Set the desired multiplication persistence

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

        while (true) {
            if (containsZeroOrFive(number) || containsTwoThrees(number) || !isAscendingOrder(number)) {
                number++;
                continue;
            }

            if (getPersistence(number) >= persistence) {
                return number;
            }

            number++;
        }
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
