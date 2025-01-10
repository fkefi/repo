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
            System.err.println("Σφάλμα βάσης δεδομένων: " + e.getMessage());
        }
    }

    private void processCandidates() throws SQLException {
        // Χρησιμοποιούμε τη μέθοδο toMap για να πάρουμε τα ονόματα
        List<String> names = reader.getFullNames();
        id = new String[names.size()][2];
        
        // Γέμισμα του πίνακα id μόνο με τα ονόματα και τους κωδικούς
        for (int i = 0; i < names.size(); i++) {
            id[i][0] = names.get(i);  // Αποθηκεύουμε το όνομα
            id[i][1] = generateRandomCode();  // Αποθηκεύουμε τον κωδικό
        }
    
        // Αποθήκευση στη βάση δεδομένων
        dbHandler.insertArray("id", id, names.size(), 2);
        
        // Εκτύπωση του πίνακα id
        System.out.println("\nΠίνακας id (Ονόματα και Κωδικοί):");
        System.out.println("----------------------------------------");
        System.out.println("Όνομα\t\t\tΚωδικός");
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

            // Αναζήτηση στον πίνακα id
            for (String[] candidate : id) {
                if (candidate[1].equals(searchCodeStr)) {
                    name = candidate[0];
                    break;
                }
            }

            // Λήψη του πίνακα finalCandidates από τη βάση
            String[][] finalCandidatesArray = dbHandler.fetchTable("finalCandidates");
            
            // Αναζήτηση θέσης στο finalCandidates
            for (int i = 0; i < finalCandidatesArray.length; i++) {
                if (finalCandidatesArray[i][0].equals(searchCode)) {
                    position = i + 1;
                    System.out.println("Βρέθηκε στη θέση: " + position + 
                                     " με βαθμό: " + finalCandidatesArray[i][1]);
                    break;
                }
            }

            if (position == -1) {
                return "Ο κωδικός δεν βρέθηκε.";
            }

            return String.format("Όνομα: %s, Θέση ταξινόμησης: %d", 
                name, position);
} catch (Exception e) {
            System.err.println("Σφάλμα αναζήτησης: " + e.getMessage());
            return "Σφάλμα κατά την αναζήτηση";
        }
    }

    private String generateRandomCode() {
        double code = Math.random() * 1000000;
        return String.format("%.0f", code);
    }
}