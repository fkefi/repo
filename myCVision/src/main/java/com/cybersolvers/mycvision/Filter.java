package com.cybersolvers.mycvision;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.sql.SQLException;
import java.net.URL;

public class Filter {
    protected String[][] id;  // Άλλαξε σε protected για πρόσβαση από υποκλάσεις
    protected Txtreader reader;  // Άλλαξε σε protected
    protected SQLiteHandler dbHandler;  // Άλλαξε σε protected
    private static boolean tableDisplayed = false;
    private static Set<String> usedCodes = new HashSet<>();
    
    protected URL resource = Filter.class.getClassLoader().getResource("my_database.db");
    protected String dbPath = resource.getPath();
    protected String dbUrl = "jdbc:sqlite:" + dbPath ;
    public Filter() {
        reader = new Txtreader();
        try {
            this.dbHandler = new SQLiteHandler(dbUrl);
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }
    }
    

    protected void processCandidates() throws SQLException {
        List<String> names = reader.getFullNames();
        id = new String[names.size()][2];

        
        // Διάβασε τους υπάρχοντες κωδικούς από τη βάση δεδομένων

            // Δημιούργησε νέους κωδικούς μόνο αν δεν υπάρχουν ήδη
            Set<String> existingCodes = new HashSet<>();
            
            for (int i = 0; i < names.size(); i++) {
                id[i][0] = names.get(i);
                String newCode;
                do {
                    newCode = generateRandomCode();
                } while (existingCodes.contains(newCode));
                
                id[i][1] = newCode;
                existingCodes.add(newCode);
            }
            
            // Αποθήκευσε στη βάση δεδομένων μόνο αν δημιουργήθηκαν νέοι κωδικοί
            dbHandler.insertStringArray("id", id);
        }
    



    public String searchByCode(double searchCode) throws SQLException {
        this.id = dbHandler.fetchStringArray("id");
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
            double[][] finalCandidatesArray = dbHandler.fetchDoubleArray("finalCandidates");
            
            // Search position in finalCandidates
            for (int i = 0; i < finalCandidatesArray.length; i++) {
                if (finalCandidatesArray[i][0] == searchCode) {
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
    protected String generateRandomCode() {
        String code;
        do {
            double randomNum = Math.random() * 1000000;
            code = String.format("%.0f", randomNum);
        } while (usedCodes.contains(code));
        usedCodes.add(code);
        return code;
    }
}

