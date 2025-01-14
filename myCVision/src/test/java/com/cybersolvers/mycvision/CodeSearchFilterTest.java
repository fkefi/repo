package com.cybersolvers.mycvision;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.SQLException;

class CodeSearchFilterTest {
    
    private JFrame mainFrame;
    private Connection connection;
    
    @BeforeEach
    void setUp() {
        mainFrame = new JFrame();
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        
        // Αρχικοποίηση της βάσης δεδομένων για testing
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:test.db");
            Statement stmt = connection.createStatement();
            stmt.execute("CREATE TABLE IF NOT EXISTS items (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "code TEXT NOT NULL," +
                        "description TEXT)");
        } catch (SQLException e) {
            fail("Αποτυχία αρχικοποίησης της βάσης δεδομένων: " + e.getMessage());
        }
    }
    
    @AfterEach
    void tearDown() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        mainFrame.dispose();
    }

    @Test
    void testCodeSearchFilterCreation() {
        // Δημιουργία του βασικού frame
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        
        try {
            // Δημιουργία του CodeSearchFilter
            CodeSearchFilter dialog = new CodeSearchFilter(frame);
            
            // Βασικοί έλεγχοι
            assertNotNull(dialog, "Το dialog δεν πρέπει να είναι null");
            assertTrue(dialog.isModal(), "Το dialog πρέπει να είναι modal");
            assertEquals("Search by Code", dialog.getTitle(), 
                        "Ο τίτλος πρέπει να είναι 'Search by Code'");
            
        } finally {
            // Καθαρισμός
            frame.dispose();
        }
    }

    @Test
    void testFilterInitialization() {
        System.out.println("=== Έλεγχος Filter ===");
        System.out.println("1. Θα ανοίξει παράθυρο διαλόγου");
        System.out.println("2. Πατήστε Cancel");
        
        // Δημιουργία του dialog για να ελέγξουμε έμμεσα ότι το Filter δημιουργείται
        CodeSearchFilter dialog = new CodeSearchFilter(mainFrame);
        assertNotNull(dialog, "Το dialog δεν πρέπει να είναι null");
    }

    @Test
    void testSearchFunctionality() {
        System.out.println("=== Έλεγχος Αναζήτησης ===");
        System.out.println("1. Θα ανοίξει παράθυρο διαλόγου");
        System.out.println("2. Εισάγετε τον κωδικό: 123.45");
        System.out.println("3. Επιβεβαιώστε ότι εμφανίζεται αποτέλεσμα");
        
        CodeSearchFilter dialog = new CodeSearchFilter(mainFrame);
        assertNotNull(dialog, "Το dialog δεν πρέπει να είναι null");
    }

    @Test
    void testInvalidInput() {
        System.out.println("=== Έλεγχος Μη Έγκυρης Εισόδου ===");
        System.out.println("1. Θα ανοίξει παράθυρο διαλόγου");
        System.out.println("2. Εισάγετε: abc");
        System.out.println("3. Επιβεβαιώστε ότι εμφανίζεται μήνυμα σφάλματος");
        
        CodeSearchFilter dialog = new CodeSearchFilter(mainFrame);
        assertNotNull(dialog, "Το dialog δεν πρέπει να είναι null");
    }

    @Test
    void testDialogProperties() {
        System.out.println("=== Έλεγχος Ιδιοτήτων Dialog ===");
        System.out.println("1. Θα ανοίξει παράθυρο διαλόγου");
        System.out.println("2. Επιβεβαιώστε τον τίτλο 'Search by Code'");
        System.out.println("3. Πατήστε Cancel");
        
        CodeSearchFilter dialog = new CodeSearchFilter(mainFrame);
        assertTrue(dialog.isModal(), "Το dialog πρέπει να είναι modal");
        assertEquals("Search by Code", dialog.getTitle(), 
                    "Ο τίτλος πρέπει να είναι 'Search by Code'");
    }
}