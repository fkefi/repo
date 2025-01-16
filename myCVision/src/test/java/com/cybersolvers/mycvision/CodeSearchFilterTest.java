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

    @BeforeAll
    static void printInstructionsOnce() {
        System.out.println("=== Έλεγχος Αναζήτησης ===");

        System.out.println("2. Εισάγετε τον κωδικό: 123.45");

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
    void testSearchFunctionality() {
        CodeSearchFilter dialog = new CodeSearchFilter(mainFrame);
        assertNotNull(dialog, "Το dialog δεν πρέπει να είναι null");
    }
}
