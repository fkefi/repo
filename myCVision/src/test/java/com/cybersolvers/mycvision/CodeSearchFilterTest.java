package com.cybersolvers.mycvision;

import org.junit.jupiter.api.Test;
import javax.swing.*;
import static org.junit.jupiter.api.Assertions.*;

public class CodeSearchFilterTest {
    
    @Test
    void testFrameCreation() {
        JFrame frame = new JFrame();
        assertNotNull(frame, "Το JFrame πρέπει να δημιουργείται επιτυχώς");
        assertEquals(JFrame.EXIT_ON_CLOSE, frame.getDefaultCloseOperation(), 
            "Το JFrame πρέπει να έχει σωστό defaultCloseOperation");
    }
    
    @Test
    void testDialogCreation() {
        JFrame frame = new JFrame();
        CodeSearchFilter dialog = new CodeSearchFilter(frame);
        assertNotNull(dialog, "Ο διάλογος πρέπει να δημιουργείται επιτυχώς");
    }
    
    @Test
    void testDialogLocation() {
        JFrame frame = new JFrame();
        CodeSearchFilter dialog = new CodeSearchFilter(frame);
        dialog.setLocationRelativeTo(null);
        
        // Ελέγχουμε αν έχει οριστεί θέση
        assertTrue(dialog.getLocation().x >= 0, "Ο διάλογος πρέπει να έχει έγκυρη θέση X");
        assertTrue(dialog.getLocation().y >= 0, "Ο διάλογος πρέπει να έχει έγκυρη θέση Y");
    }
    
    @Test
    void testExceptionHandling() {
        Exception testException = new Exception("Δοκιμαστικό σφάλμα");
assertDoesNotThrow(() -> {
            JOptionPane.showMessageDialog(null,
                "Σφάλμα κατά την εκκίνηση: " + testException.getMessage(),
                "Σφάλμα",
                JOptionPane.ERROR_MESSAGE);
        }, "Ο χειρισμός σφαλμάτων πρέπει να λειτουργεί σωστά");
    }
}