package com.cybersolvers.mycvision;

import org.junit.Before;
import org.junit.Test;
import org.junit.After;
import static org.junit.Assert.*;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class FilterTest {
    private Filter filter;
    private SQLiteHandler dbHandler;
    
    @Before
    public void setUp() throws SQLException, ClassNotFoundException {
        filter = new Filter();
        CVSubmissionApp.initializeCVFolder();
        try {
            dbHandler = new SQLiteHandler(filter.dbUrl);
        } catch (SQLException e) {
            fail("Database setup failed: " + e.getMessage());
        }
    }
    
    @After
    public void tearDown() {
        try {
            if (dbHandler != null) {
                dbHandler.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing database connection: " + e.getMessage());
        }
    }
    
    @Test
    public void testGenerateRandomCode() {
        // Test that generated codes are unique
        Set<String> codes = new HashSet<>();
        for (int i = 0; i < 100; i++) {
            String code = filter.generateRandomCode();
            // Check code format
            assertTrue("Code should be numeric", code.matches("\\d+"));
            assertTrue("Code length should be reasonable", code.length() > 0 && code.length() <= 7);
            // Check uniqueness
            assertTrue("Code should be unique", codes.add(code));
        }
    }
    
    @Test
    public void testProcessCandidates() {
        try {
            // Process candidates
            filter.processCandidates();
            
            // Fetch the stored data
            String[][] idArray = dbHandler.fetchStringArray("id");
            
            // Verify the data
            assertNotNull("ID array should not be null", idArray);
            assertTrue("ID array should not be empty", idArray.length > 0);
            
            // Check structure of stored data
            for (String[] row : idArray) {
                assertEquals("Each row should have 2 columns", 2, row.length);
                assertNotNull("Name should not be null", row[0]);
                assertNotNull("Code should not be null", row[1]);
                assertTrue("Code should be numeric", row[1].matches("\\d+"));
            }
        } catch (SQLException e) {
            fail("ProcessCandidates test failed: " + e.getMessage());
        }
    }
    
    @Test
    public void testSearchByCode() {
        try {
            // First ensure we have data to search
            filter.processCandidates();
            
            // Insert test data into finalCandidates table
            double[][] testData = new double[][] {
                {123456, 95.5},
                {234567, 88.0}
            };
            dbHandler.insertDoubleArray("finalCandidates", testData);
            
            // Test searching with valid code
            String result = filter.searchByCode(123456);
            assertNotNull("Search result should not be null", result);
            assertFalse("Search result should not be empty", result.isEmpty());
            
            // Test searching with invalid code
            result = filter.searchByCode(-1);
            assertEquals("Code not found.", result);
        } catch (SQLException e) {
            fail("SearchByCode test failed: " + e.getMessage());
        }
    }
    
    @Test
    public void testDatabaseOperations() {
        try {
            // Test string array operations
            String[][] testStringArray = new String[][] {
                {"Test1", "Code1"},
                {"Test2", "Code2"}
            };
            dbHandler.insertStringArray("test_table", testStringArray);
            String[][] retrievedStringArray = dbHandler.fetchStringArray("test_table");
            
            assertNotNull("Retrieved string array should not be null", retrievedStringArray);
            assertEquals("Array should have same number of rows", 
                testStringArray.length, retrievedStringArray.length);
            
            // Test double array operations
            double[][] testDoubleArray = new double[][] {
                {1.0, 2.0},
                {3.0, 4.0}
            };
            dbHandler.insertDoubleArray("test_double_table", testDoubleArray);
            double[][] retrievedDoubleArray = dbHandler.fetchDoubleArray("test_double_table");
            
            assertNotNull("Retrieved double array should not be null", retrievedDoubleArray);
            assertEquals("Array should have same number of rows", 
                testDoubleArray.length, retrievedDoubleArray.length);
            
        } catch (SQLException e) {
            fail("Database operations test failed: " + e.getMessage());
        }
    }
}

