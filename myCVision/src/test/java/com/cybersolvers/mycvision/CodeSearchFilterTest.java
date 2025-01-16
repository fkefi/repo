package com.cybersolvers.mycvision;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import javax.swing.*;

class CodeSearchFilterTest {
    private JFrame mainFrame;
    private CodeSearchFilter searchFilter;

    @BeforeEach
void setUp() {
    CVSubmissionApp.initializeCVFolder(); // Αρχικοποίηση cvFolder
    mainFrame = new JFrame();
    mainFrame.setTitle("Test Frame"); // Ορίζουμε τον τίτλο
    mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    mainFrame.pack();
    searchFilter = new CodeSearchFilter(mainFrame);
}


    @AfterEach
    void tearDown() {
        if (searchFilter != null) {
            searchFilter.dispose();
        }
        if (mainFrame != null) {
            mainFrame.dispose();
        }
    }

    @Test
    void testFrameProperties() {
        // Έλεγχος βασικών ιδιοτήτων του frame
        assertNotNull(mainFrame, "Το mainFrame δεν πρέπει να είναι null");
        
        assertEquals(WindowConstants.EXIT_ON_CLOSE, mainFrame.getDefaultCloseOperation(), 
                    "Το JFrame πρέπει να έχει σωστό defaultCloseOperation");
        
        assertNotNull(mainFrame.getContentPane(), 
                    "Το frame πρέπει να έχει content pane");
                
        assertEquals("Test Frame", mainFrame.getTitle(),
                    "Το frame πρέπει να έχει το σωστό τίτλο");
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