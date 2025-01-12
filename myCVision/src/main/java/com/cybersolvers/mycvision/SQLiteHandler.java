package com.cybersolvers.mycvision;
import java.sql.*;
import java.util.*;

public class SQLiteHandler {

    private Connection connection;

    public SQLiteHandler() throws Exception {
        this.connection = connect();
    }

    public Connection connect() throws Exception {

        String dbPath = this.getClass().getClassLoader().getResource("database.db").getPath();

        String url = "jdbc:sqlite:" + dbPath;

        return DriverManager.getConnection(url);
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
    public Map<String, Object> fetchJsonAsMap(String tableName) throws SQLException {
        String query = "SELECT * FROM " + tableName;
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            Map<String, Object> map = new HashMap<>();
            while (rs.next()) {
                String key = rs.getString("key"); 
                Object value = rs.getObject("value"); 
                map.put(key, value);
            }
            return map;
        }
    }

    // Method 5: Insert a nested Map into the database
    public void insertNestedMap(String tableName, Map<String, Map<String, Object>> nestedMap) throws SQLException {
        String createTableQuery = "CREATE TABLE IF NOT EXISTS " + tableName + " (outer_key TEXT, inner_key TEXT, value TEXT);";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createTableQuery);
        }

        String insertQuery = "INSERT INTO " + tableName + " (outer_key, inner_key, value) VALUES (?, ?, ?);";
        try (PreparedStatement pstmt = connection.prepareStatement(insertQuery)) {
            for (Map.Entry<String, Map<String, Object>> outerEntry : nestedMap.entrySet()) {
                String outerKey = outerEntry.getKey();
                for (Map.Entry<String, Object> innerEntry : outerEntry.getValue().entrySet()) {
                    pstmt.setString(1, outerKey);
                    pstmt.setString(2, innerEntry.getKey());
                    pstmt.setString(3, innerEntry.getValue().toString());
                    pstmt.executeUpdate();
                }
            }
        }
    }

    // Method 6: Retrieve a nested Map from the database
    public Map<String, Map<String, Object>> fetchNestedMap(String tableName) throws SQLException {
        String query = "SELECT * FROM " + tableName;
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            Map<String, Map<String, Object>> nestedMap = new HashMap<>();
            while (rs.next()) {
                String outerKey = rs.getString("outer_key");
                String innerKey = rs.getString("inner_key");
                Object value = rs.getObject("value");

                nestedMap.putIfAbsent(outerKey, new HashMap<>());
                nestedMap.get(outerKey).put(innerKey, value);
            }
            return nestedMap;
        }
    }

    // Method 7 : Insert a nested Map with Integer values into the database
    public void insertNestedIntegerData(String tableName, Map<String, Map<String, Integer>> nestedMap) throws SQLException {
    String createTableQuery = "CREATE TABLE IF NOT EXISTS " + tableName + " (outer_key TEXT, inner_key TEXT, int_value INTEGER);";
    try (Statement stmt = connection.createStatement()) {
        stmt.execute(createTableQuery);
    }

    String insertQuery = "INSERT INTO " + tableName + " (outer_key, inner_key, int_value) VALUES (?, ?, ?);";
    try (PreparedStatement pstmt = connection.prepareStatement(insertQuery)) {
        for (Map.Entry<String, Map<String, Integer>> outerEntry : nestedMap.entrySet()) {
            String outerKey = outerEntry.getKey();
            for (Map.Entry<String, Integer> innerEntry : outerEntry.getValue().entrySet()) {
                pstmt.setString(1, outerKey);
                pstmt.setString(2, innerEntry.getKey());
                pstmt.setInt(3, innerEntry.getValue()); // Εισαγωγή ακέραιου
                pstmt.executeUpdate();
            }
        }
    }
}

// Method 8: Retrieve a nested Map with Integer values from the database
    public Map<String, Map<String, Integer>> fetchNestedIntegerData(String tableName) throws SQLException {
    String query = "SELECT * FROM " + tableName;
    try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
        Map<String, Map<String, Integer>> nestedMap = new HashMap<>();
        while (rs.next()) {
            String outerKey = rs.getString("outer_key");
            String innerKey = rs.getString("inner_key");
            int value = rs.getInt("int_value");

            nestedMap.putIfAbsent(outerKey, new HashMap<>());
            nestedMap.get(outerKey).put(innerKey, value);
        }
        return nestedMap;
    }
}

// Method 9 : Insert a String[][] into the database
public void insertStringArray(String tableName, String[][] array) throws SQLException {
    String createTableQuery = "CREATE TABLE IF NOT EXISTS " + tableName + " (row_index INTEGER, col_index INTEGER, value TEXT);";
    try (Statement stmt = connection.createStatement()) {
        stmt.execute(createTableQuery);
    }

    String insertQuery = "INSERT INTO " + tableName + " (row_index, col_index, value) VALUES (?, ?, ?);";
    try (PreparedStatement pstmt = connection.prepareStatement(insertQuery)) {
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                pstmt.setInt(1, i);
                pstmt.setInt(2, j);
                pstmt.setString(3, array[i][j]);
                pstmt.executeUpdate();
            }
        }
    }
}
    // Method: Fetch a double[][] from the database
public double[][] fetchDoubleArray(String tableName) throws SQLException {
    String query = "SELECT * FROM " + tableName;
    try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
        List<int[]> indicesList = new ArrayList<>();
        List<Double> values = new ArrayList<>();

        while (rs.next()) {
            int row = rs.getInt("row_index");
            int col = rs.getInt("col_index");
            double value = rs.getDouble("value");
            indicesList.add(new int[]{row, col});
            values.add(value);
        }

        int maxRows = indicesList.stream().mapToInt(idx -> idx[0] + 1).max().orElse(0);
        int maxCols = indicesList.stream().mapToInt(idx -> idx[1] + 1).max().orElse(0);
        double[][] array = new double[maxRows][maxCols];

        for (int i = 0; i < indicesList.size(); i++) {
            int[] indices = indicesList.get(i);
            array[indices[0]][indices[1]] = values.get(i);
        }
        return array;
    }
}
   
    // Method: Fetch a double[] from the database
public double[] fetchDouble1DArray(String tableName) throws SQLException {
    String query = "SELECT * FROM " + tableName + " ORDER BY row_index";
    try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
        List<Double> values = new ArrayList<>();
        while (rs.next()) {
            values.add(rs.getDouble("value"));
        }
        return values.stream().mapToDouble(Double::doubleValue).toArray();
    }
}
    

    // Method: Fetch a String[][] from the database
public String[][] fetchStringArray(String tableName) throws SQLException {
    String query = "SELECT * FROM " + tableName;
    try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
        List<int[]> indicesList = new ArrayList<>();
        List<String> values = new ArrayList<>();

        while (rs.next()) {
            int row = rs.getInt("row_index");
            int col = rs.getInt("col_index");
            String value = rs.getString("value");
            indicesList.add(new int[]{row, col});
            values.add(value);
        }

        int maxRows = indicesList.stream().mapToInt(idx -> idx[0] + 1).max().orElse(0);
        int maxCols = indicesList.stream().mapToInt(idx -> idx[1] + 1).max().orElse(0);
        String[][] array = new String[maxRows][maxCols];

        for (int i = 0; i < indicesList.size(); i++) {
            int[] indices = indicesList.get(i);
            array[indices[0]][indices[1]] = values.get(i);
        }
        return array;
    }
}
     
     // Method: Fetch a Map<String, Map<String, Integer>> from the database
public Map<String, Map<String, Integer>> fetchMapFromDatabase(String tableName) throws SQLException {
    String query = "SELECT * FROM " + tableName;
    try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
        Map<String, Map<String, Integer>> map = new HashMap<>();
        while (rs.next()) {
            String outerKey = rs.getString("outer_key");
            String innerKey = rs.getString("inner_key");
            int value = rs.getInt("int_value");

            map.computeIfAbsent(outerKey, k -> new HashMap<>()).put(innerKey, value);
        }
        return map;
    }
}
  

    // Method: Insert a double[][] into the database
public void insertDoubleArray(String tableName, double[][] array) throws SQLException {
    String createTableQuery = "CREATE TABLE IF NOT EXISTS " + tableName + " (row_index INTEGER, col_index INTEGER, value REAL);";
    try (Statement stmt = connection.createStatement()) {
        stmt.execute(createTableQuery);
    }

    String insertQuery = "INSERT INTO " + tableName + " (row_index, col_index, value) VALUES (?, ?, ?);";
    try (PreparedStatement pstmt = connection.prepareStatement(insertQuery)) {
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                pstmt.setInt(1, i);
                pstmt.setInt(2, j);
                pstmt.setDouble(3, array[i][j]);
                pstmt.executeUpdate();
            }
        }
    }
}



     








    // Close the connection
    public void close() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

}
