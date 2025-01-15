package com.cybersolvers.mycvision;

import org.junit.jupiter.api.*;
import java.sql.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class SQLiteHandlerTest {

    private SQLiteHandler dbHandler;
    private static final String TEST_DB_URL = "jdbc:sqlite::memory:"; 

    @BeforeEach
    void setUp() throws SQLException {
        dbHandler = new SQLiteHandler(TEST_DB_URL);
    }

    @AfterEach
    void tearDown() throws SQLException {
        
        dbHandler.close();
    }

    @Test
    void testInsert1DStringArray() throws SQLException {
        String tableName = "test_string_array";
        String[] testArray = {"Apple", "Banana", "Cherry"};

       
        dbHandler.insert1DStringArray(tableName, testArray);

       
        String[] resultArray = dbHandler.fetchTableAsStringArray(tableName);

        
        assertArrayEquals(testArray, resultArray, "data  are ok!");
    }

    @Test
    void testInsert1DdoubleArray() throws SQLException {
        String tableName = "test_double_array";
        double[] testArray = {1.1, 2.2, 3.3};

        
        dbHandler.insert1DdoubleArray(tableName, testArray);

       
        double[] resultArray = dbHandler.fetchDouble1DArray(tableName);

       
        assertArrayEquals(testArray, resultArray, 0.001, "data are not ok");
    }

    @Test
    void testInsertStringArray() throws SQLException {
        String tableName = "test_2d_string_array";
        String[][] testArray = {
                {"A1", "A2", "A3"},
                {"B1", "B2", "B3"}
        };

        // Εισαγωγή δεδομένων
        dbHandler.insertStringArray(tableName, testArray);

        // Ανάκτηση δεδομένων
        String[][] resultArray = dbHandler.fetchStringArray(tableName);

        // Έλεγχος των αποτελεσμάτων
        assertArrayEquals(testArray, resultArray, "Τα δεδομένα που ανακτήθηκαν δεν είναι σωστά!");
    }

    @Test
    void testInsertDoubleArray() throws SQLException {
        String tableName = "test_2d_double_array";
        double[][] testArray = {
                {1.1, 1.2, 1.3},
                {2.1, 2.2, 2.3}
        };

        
        dbHandler.insertDoubleArray(tableName, testArray);

        
        double[][] resultArray = dbHandler.fetchDoubleArray(tableName);

        
        assertArrayEquals(testArray, resultArray, "data are not ok");
    }

    @Test
    void testInsertNestedIntegerData() throws SQLException {
        String tableName = "test_nested_map";
        Map<String, Map<String, Integer>> testMap = new HashMap<>();
        Map<String, Integer> innerMap1 = new HashMap<>();
        innerMap1.put("key1", 10);
        innerMap1.put("key2", 20);
        testMap.put("outer1", innerMap1);

        Map<String, Integer> innerMap2 = new HashMap<>();
        innerMap2.put("keyA", 30);
        innerMap2.put("keyB", 40);
        testMap.put("outer2", innerMap2);

       
        dbHandler.insertNestedIntegerData(tableName, testMap);

       
        Map<String, Map<String, Integer>> resultMap = dbHandler.fetchMapFromDatabase(tableName);

        
        assertEquals(testMap, resultMap, "data are not ok");
    }

    @Test
    void testFetchEmptyTable() throws SQLException {
        String tableName = "empty_table";
        dbHandler.insert1DStringArray(tableName, new String[]{});

        String[] resultArray = dbHandler.fetchTableAsStringArray(tableName);
        assertEquals(0, resultArray.length, "table isn't empty ");
    }
}
