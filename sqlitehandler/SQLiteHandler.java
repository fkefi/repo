package com.despoina.sqlitehandler;
import java.sql.*;
import java.util.*;

public class SQLiteHandler {

    private Connection connection;

    // Constructor: Connects to the SQLite database
    public SQLiteHandler(String dbUrl) throws SQLException {
        connection = DriverManager.getConnection(dbUrl);
    }

    // Method 1: Insert a multidimensional array into the database
    public <T> void insertArray(String tableName, T array, int... dimensions) throws SQLException {
        StringBuilder createTableQuery = new StringBuilder("CREATE TABLE IF NOT EXISTS ").append(tableName).append(" (");
        for (int i = 0; i < dimensions.length; i++) {
            createTableQuery.append("dim").append(i).append(" INTEGER, ");
        }
        createTableQuery.append("value TEXT");
        createTableQuery.append(");");

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createTableQuery.toString());
        }

        String insertQuery = "INSERT INTO " + tableName + " (" + 
                             String.join(", ", Arrays.stream(dimensions).mapToObj(i -> "dim" + i).toArray(String[]::new)) + ", value) VALUES (" + 
                             String.join(", ", Collections.nCopies(dimensions.length + 1, "?")) + ");";
        try (PreparedStatement pstmt = connection.prepareStatement(insertQuery)) {
            insertRecursive(array, pstmt, new int[0]);
        }
    }

    private <T> void insertRecursive(T array, PreparedStatement pstmt, int[] indices) throws SQLException {
        if (array instanceof Object[]) {
            Object[] objArray = (Object[]) array;
            for (int i = 0; i < objArray.length; i++) {
                int[] newIndices = Arrays.copyOf(indices, indices.length + 1);
                newIndices[indices.length] = i;
                insertRecursive(objArray[i], pstmt, newIndices);
            }
        } else {
            for (int i = 0; i < indices.length; i++) {
                pstmt.setInt(i + 1, indices[i]);
            }
            pstmt.setString(indices.length + 1, array.toString());
            pstmt.executeUpdate();
        }
    }

    // Method 2: Retrieve an array from the database
    public Object fetchTable(String tableName) throws SQLException {
        String query = "SELECT * FROM " + tableName;
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
    
            // Detect the maximum dimensions
            int maxDim = columnCount - 1; // Exclude the "value" column
            List<int[]> indicesList = new ArrayList<>();
            List<Object> values = new ArrayList<>();
    
            // Retrieve all rows and store indices and values
            while (rs.next()) {
                int[] indices = new int[maxDim];
                for (int i = 0; i < maxDim; i++) {
                    indices[i] = rs.getInt(i + 1);
                }
                indicesList.add(indices);
                values.add(rs.getObject(columnCount)); // Keep the exact type from the database
            }
    
            // Calculate dimensions of the array
            int[] dimensions = new int[maxDim];
            for (int[] indices : indicesList) {
                for (int i = 0; i < maxDim; i++) {
                    dimensions[i] = Math.max(dimensions[i], indices[i] + 1);
                }
            }
    
            // Detect the type dynamically based on the first value
            Class<?> valueClass = values.isEmpty() ? Object.class : values.get(0).getClass();
    
            // Create the array dynamically
            Object array = java.lang.reflect.Array.newInstance(valueClass, dimensions);
            for (int i = 0; i < indicesList.size(); i++) {
                int[] indices = indicesList.get(i);
                Object value = values.get(i);
    
                // Navigate to the correct position in the array
                Object current = array;
                for (int j = 0; j < indices.length - 1; j++) {
                    current = java.lang.reflect.Array.get(current, indices[j]);
                }
                java.lang.reflect.Array.set(current, indices[indices.length - 1], value);
            }
    
            return array;
        }
    }
    

    // Method 3: Insert a JSON object as a Map into the database
    public void insertJsonAsMap(String tableName, Map<String, Object> map) throws SQLException {
        String createTableQuery = "CREATE TABLE IF NOT EXISTS " + tableName + " (key TEXT, value TEXT);";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createTableQuery);
        }

        String insertQuery = "INSERT INTO " + tableName + " (key, value) VALUES (?, ?);";
        try (PreparedStatement pstmt = connection.prepareStatement(insertQuery)) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                pstmt.setString(1, entry.getKey());
                pstmt.setString(2, entry.getValue().toString());
                pstmt.executeUpdate();
            }
        }
    }

    // Method 4: Retrieve a JSON object as a Map from the database
    public Map<String, String> fetchJsonAsMap(String tableName) throws SQLException {
        String query = "SELECT * FROM " + tableName;
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            Map<String, String> map = new HashMap<>();
            while (rs.next()) {
                map.put(rs.getString("key"), rs.getString("value"));
            }
            return map;
        }
    }

    // Close the connection
    public void close() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    public static void main(String[] args) {
        try {
            SQLiteHandler handler = new SQLiteHandler("jdbc:sqlite:sample.db");

            Double[][][] array = {
                {
                    {1.1, 1.2},
                    {1.3, 1.4}
                },
                {
                    {2.1, 2.2},
                    {2.3, 2.4}
                }
            };
            handler.insertArray("test_table", array, 2, 2, 2);
    
            // Ανάκτηση δεδομένων
            Object fetched = handler.fetchTable("test_table"); // Επιστρέφεται το αντικείμενο όπως είναι
            System.out.println(Arrays.deepToString((Object[]) fetched)); 

            // Example 2: Insert and fetch JSON as Map
            Map<String, Object> jsonMap = Map.of("key1", "value1", "key2", "value2");
            handler.insertJsonAsMap("json_table", jsonMap);
            Map<String, String> fetchedMap = handler.fetchJsonAsMap("json_table");
            System.out.println(fetchedMap);

            handler.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
