package com.cybersolvers.mycvision;

import java.util.List;
import java.sql.SQLException;

public class Filter {
    private String[][] id;
    private Txtreader reader;
    private SQLiteHandler dbHandler;

    public Filter() {
        reader = new Txtreader();
        try {
            dbHandler = new SQLiteHandler("my_database.db");
            processCandidates();
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }
    }

    private void processCandidates() throws SQLException {
        // Using the toMap method to get the names
        List<String> names = reader.getFullNames();
        id = new String[names.size()][2];
        
        // Filling the id array only with the names and codes
        for (int i = 0; i < names.size(); i++) {
            id[i][0] = names.get(i);  // Store the name
            id[i][1] = generateRandomCode();  // Store the code
        }
    
        // Save to the database
        dbHandler.insertArray("id", id, names.size(), 2);
        
        // Print the id array
        System.out.println("\nId Table (Names and Codes):");
        System.out.println("----------------------------------------");
        System.out.println("Name\t\t\tCode");
        System.out.println("----------------------------------------");
        for (String[] candidate : id) {
            System.out.printf("%s\t\t%s\n", candidate[0], candidate[1]);
        }
        System.out.println("----------------------------------------\n");
    }

    public String searchByCode(double searchCode) {
        try {
            String searchCodeStr = String.format("%.0f", searchCode);
            String name = "";
            int position = -1;

            // Search in the id array
            for (String[] candidate : id) {
                if (candidate[1].equals(searchCodeStr)) {
                    name = candidate[0];
                    break;
                }
            }

            // Fetch the finalCandidates table from the database
            Double[][] finalCandidatesArray = (Double[][]) dbHandler.fetchTable("finalCandidates");
            
            // Search position in finalCandidates
            for (int i = 0; i < finalCandidatesArray.length; i++) {
                if (finalCandidatesArray[i][0].equals(searchCode)) {
                    position = i + 1;
                    System.out.println("Found at position: " + position + 
                                     " with score: " + finalCandidatesArray[i][1]);
                    break;
                }
            }

            if (position == -1) {
                return "Code not found.";
            }

            return String.format("Name: %s, Ranking position: %d", 
                name, position);
        } catch (Exception e) {
            System.err.println("Search error: " + e.getMessage());
            return "Error during search";
        }
    }

    private String generateRandomCode() {
        double code = Math.random() * 1000000;
        return String.format("%.0f", code);
    }
}