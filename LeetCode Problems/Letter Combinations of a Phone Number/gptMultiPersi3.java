public class gptMultiPersi3 {
    public static void main(String[] args) {
        PersistenceCacheManager cacheManager = new PersistenceCacheManager();

        int persistence = 8;
        long smallestNumber = findSmallestNumberWithPersistence(persistence, cacheManager);
        System.out.println("The smallest number with a multiplication persistence of " + persistence + " is: " + smallestNumber);
    }

    public static long findSmallestNumberWithPersistence(int persistence, PersistenceCacheManager cacheManager) {
        long number = 1;
        while (cacheManager.getPersistence(number) < persistence) {
            number++;
        }
        return number;
    }
}