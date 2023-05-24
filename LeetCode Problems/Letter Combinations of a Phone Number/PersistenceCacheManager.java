import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.lang.Class;


public class PersistenceCacheManager {

    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/";
    private static final String DB_USER = "intelij";
    private static final String DB_PASSWORD = "Wa1smeSqPa?";

    private Map<Long, Integer> persistenceCache;

    public PersistenceCacheManager() {
        persistenceCache = loadPersistenceCache();
    }

    private Map<Long, Integer> loadPersistenceCache() {
        Map<Long, Integer> cache = new HashMap<>();

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement statement = connection.createStatement()) {
            String query = "SELECT number, persistence FROM cache";
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                long number = resultSet.getLong("number");
                int persistence = resultSet.getInt("persistence");
                cache.put(number, persistence);
            }
        } catch (SQLException e) {
            // Handle database access error
            e.printStackTrace();
        }

        return cache;
    }

    private void savePersistenceCache() {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement statement = connection.createStatement()) {
            String createTableQuery = "CREATE TABLE IF NOT EXISTS cache (number BIGINT, persistence INT)";
            statement.executeUpdate(createTableQuery);

            String truncateTableQuery = "TRUNCATE TABLE cache";
            statement.executeUpdate(truncateTableQuery);

            String insertQuery = "INSERT INTO cache (number, persistence) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);

            for (Map.Entry<Long, Integer> entry : persistenceCache.entrySet()) {
                long number = entry.getKey();
                int persistence = entry.getValue();
                preparedStatement.setLong(1, number);
                preparedStatement.setInt(2, persistence);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            // Handle database access error
            e.printStackTrace();
        }
    }

    public int getPersistence(long number) {
        if (persistenceCache.containsKey(number)) {
            return persistenceCache.get(number);
        }

        int persistence = calculatePersistence(number);
        persistenceCache.put(number, persistence);
        savePersistenceCache();
        return persistence;
    }

    private int calculatePersistence(long number) {
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
