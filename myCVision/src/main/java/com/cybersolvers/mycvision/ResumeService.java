package com.cybersolvers.mycvision;

import java.util.*;
import java.sql.SQLException;

public class ResumeService {
    private final String[] levels = {"TELEIA", "POLY KALA", "KALA", "OXI"};
    
    private final String dburl = "C:\\cygwin64\\home\\efifk\\repo\\myCVision\\src\\main\\resources";
    private final SQLiteHandler sqlitehandler;
    
    private final String universities;
    private final String workExperience;
    private final String bachelorDept;
    private final String masterDept;
    private final String phDDept;
    
    private final List<String[]> arrays;
    private final List<String> tableNames;
    
    // Constructor
    public ResumeService() throws SQLException {
        this.sqlitehandler = new SQLiteHandler(dburl);
        
        // Initialize database values
        this.universities = sqlitehandler.fetchTable("universities");
        this.workExperience = sqlitehandler.fetchTable("workExperience");
        this.bachelorDept = sqlitehandler.fetchTable("bachelorDept");
        this.masterDept = sqlitehandler.fetchTable("masterDept");
        this.phDDept = sqlitehandler.fetchTable("phDDept");
        
        // Initialize lists
        this.arrays = new ArrayList<>();
        this.tableNames = new ArrayList<>();
        
        // Add items to arrays
        arrays.add(universities.split(","));  // Υποθέτω ότι το fetchTable επιστρέφει comma-separated string
        arrays.add(levels);
        arrays.add(workExperience.split(","));
        arrays.add(bachelorDept.split(","));
        arrays.add(masterDept.split(","));
        arrays.add(phDDept.split(","));
        arrays.add(yesNo);
        
        // Add items to tableNames
        tableNames.add("universities");
        tableNames.add("levels");
        tableNames.add("workExperience");
        tableNames.add("bachelorDept");
        tableNames.add("masterDept");
        tableNames.add("phDDept");
        tableNames.add("yesNo");
    }
    
    // Μέθοδος για την αξιολόγηση των κριτηρίων
    public Map<String, Integer> evaluateCriteria(String[] array, String tableName) {
        Map<String, Integer> scores = new LinkedHashMap<>();
        
        // Γέμισμα του Map
        if (!"universities".equals(tableName) && 
            !"workExperience".equals(tableName) && 
            !"bachelorDept".equals(tableName) && 
            !"masterDept".equals(tableName) && 
            !"phDDept".equals(tableName)) {
            
            for (int i = 0; i <= array.length - 1; i++) {
                scores.put(array[i], array.length - 1 - i);
            }
        } else if ("workExperience".equals(tableName)) {
            for (int i = 0; i < array.length - 1; i++) {
                scores.put(array[i], i + 1);
            }
        } else if ("universities".equals(tableName) || 
                   "bachelorDept".equals(tableName) || 
                   "masterDept".equals(tableName) || 
                   "phDDept".equals(tableName)) {
            
            int maxScore = array.length + 1;
            for (int i = 0; i < array.length; i++) {
                scores.put(array[i], maxScore - i);
            }
        }
        
        if ("universities".equals(tableName) || 
            "degreeDept".equals(tableName) || 
            "masterDept".equals(tableName) || 
            "phDDept".equals(tableName)) {
            
            scores.put("asxeto", 1);
            scores.put("den exei spoudasei", 0);
        }
        
        if ("workExperience".equals(tableName)) {
            scores.put("more years", 8);
        }
        
        return scores;
    }
    
    // Μέθοδος για την επεξεργασία όλων των πινάκων
    public Map<String, Map<String, Integer>> evaluateMultipleTablesToJson() throws SQLException {
        Map<String, Map<String, Integer>> allTablesData = new LinkedHashMap<>();
        
        if (arrays.size() != tableNames.size()) {
            throw new IllegalArgumentException("Οι λίστες των arrays και των tableNames πρέπει να έχουν το ίδιο μέγεθος");
        }
        
        for (int i = 0; i < arrays.size(); i++) {
            String tableName = tableNames.get(i);
            String[] array = arrays.get(i);
            Map<String, Integer> tableData = evaluateCriteria(array, tableName);
            allTablesData.put(tableName, tableData);
        }
        
        // Αποθήκευση των δεδομένων στη βάση
        sqlitehandler.insertNestedMap("allTablesData", allTablesData);
        
        return allTablesData;
    }
}