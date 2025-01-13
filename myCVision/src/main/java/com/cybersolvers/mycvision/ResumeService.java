package com.cybersolvers.mycvision;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ResumeService {
    public final String[] levels = {"TELEIA", "POLY KALA", "KALA", "OXI"};
    

    URL resource = ResumeService.class.getClassLoader().getResource("mydatabase.db");
    String dbPath = resource.getPath();
    String dbUrl = "jdbc:sqlite:" + dbPath;


    private final SQLiteHandler sqlitehandler;
    
    public final String universities;
    public final String workExperience;
    public final String bachelorDept;
    public final String masterDept;
    public final String phDDept;
    
    public  final List<String[]> arrays;
    public final List<String> tableNames;
    
    // Constructor
    public ResumeService() throws SQLException {
        this.sqlitehandler = new SQLiteHandler(dbUrl);
        
        // Initialize database values
        this.universities = (String) sqlitehandler.fetchTable("universities");
        this.workExperience = (String) sqlitehandler.fetchTable("workExperience");
        this.bachelorDept = (String) sqlitehandler.fetchTable("bachelorDept");
        this.masterDept = (String) sqlitehandler.fetchTable("masterDept");
        this.phDDept = (String) sqlitehandler.fetchTable("phDDept");
        
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
        
        // Add items to tableNames
        tableNames.add("universities");
        tableNames.add("levels");
        tableNames.add("workExperience");
        tableNames.add("bachelorDept");
        tableNames.add("masterDept");
        tableNames.add("phDDept");

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
        
        

        
        sqlitehandler.insertNestedIntegerData("allTablesData", allTablesData);
        
        return allTablesData;
    }

    @SuppressWarnings("unused")
    Map<String, Map<String, Integer>> result = evaluateMultipleTablesToJson();

}