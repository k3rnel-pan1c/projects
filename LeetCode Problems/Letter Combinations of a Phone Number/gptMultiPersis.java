import java.util.Scanner;

public class gptMultiPersis {
    public static void main(String[] args) {
        //Scanner readInput = new Scanner(System.in);
        //System.out.println("What should the multiplicative persistence be?:");
        //int persistence = readInput.nextInt(); // Set the desired multiplication persistence
        int persistence =10;
        long smallestNumber = findSmallestNumberWithPersistence(persistence);
        System.out.println("The smallest number with a multiplication persistence of " + persistence + " is: " + smallestNumber);
    }

    public static long findSmallestNumberWithPersistence(int persistence) {
        long number = 1;
        while (getPersistence(number) < persistence) {
            number++;
        }
        return number;
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
}
