package com.cybersolvers.mycvision;
import java.sql.*;
import java.util.*;

public class SQLiteHandler {

    private Connection connection;

     // Constructor: Connects to the SQLite database
     public SQLiteHandler(String dbUrl) throws SQLException {
        connection = DriverManager.getConnection(dbUrl);
    }


    // method to insert a 1D  String array into database
    public void insert1DStringArray(String tableName, String[] array) throws SQLException {
        
        String createTableQuery = "CREATE TABLE IF NOT EXISTS " + tableName +
                " (indx INTEGER PRIMARY KEY, value TEXT);";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createTableQuery);
        }

        String insertQuery = "INSERT INTO " + tableName + " (indx, value) VALUES (?, ?);";
        try (PreparedStatement pstmt = connection.prepareStatement(insertQuery)) {
            for (int i = 0; i < array.length; i++) {
                pstmt.setInt(1, i);           
                pstmt.setString(2, array[i]); 
                pstmt.executeUpdate();
            }
        }

    }

    // method to insert a double 1D array into the database
    public void insert1DdoubleArray(String tableName, double[] array) throws SQLException {
        
        String createTableQuery = "CREATE TABLE IF NOT EXISTS " + tableName +
                " (indx INTEGER PRIMARY KEY, value REAL);";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createTableQuery);
        }

        
        String insertQuery = "INSERT INTO " + tableName + " (indx, value) VALUES (?, ?);";
        try (PreparedStatement pstmt = connection.prepareStatement(insertQuery)) {
            for (int i = 0; i < array.length; i++) {
                pstmt.setInt(1, i);           
                pstmt.setDouble(2, array[i]); 
                pstmt.executeUpdate();
            }
        }
    }

     // Method Retrieve an array from the database
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
