package com.cybersolvers.mycvision;

import org.junit.jupiter.api.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import static org.junit.jupiter.api.Assertions.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class FilterTest {
    
    private JFrame frame;
    private CodeSearchFilter dialog;
    
    @BeforeEach
    void setUp() {
        // Βεβαιωνόμαστε ότι τρέχουμε στο EDT
        SwingUtilities.invokeLater(() -> {
            frame = new JFrame();
            dialog = new CodeSearchFilter(frame);
        });
    }
    
    @AfterEach
    void tearDown() {
        if (dialog != null) {
            dialog.dispose();
        }
        if (frame != null) {
            frame.dispose();
        }
    }

    @Test
    void testDialogCreation() throws InterruptedException {
CountDownLatch latch = new CountDownLatch(1);
        
        SwingUtilities.invokeLater(() -> {
            assertNotNull(dialog, "Ο διάλογος δεν θα πρέπει να είναι null");
            assertTrue(dialog instanceof JDialog, "Ο διάλογος πρέπει να είναι τύπου JDialog");
            latch.countDown();
        });
        
        assertTrue(latch.await(5, TimeUnit.SECONDS), "Το test έληξε χρονικά");
    }
    
    @Test
    void testDialogCentering() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        
        SwingUtilities.invokeLater(() -> {
            dialog.setLocationRelativeTo(null);
            Point location = dialog.getLocation();
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            
            // Ελέγχουμε αν ο διάλογος είναι περίπου στο κέντρο της οθόνης
            assertTrue(location.x > screenSize.width/4 && 
                      location.x < screenSize.width*3/4,
                      "Ο διάλογος πρέπει να είναι κεντραρισμένος οριζόντια");
            assertTrue(location.y > screenSize.height/4 && 
                      location.y < screenSize.height*3/4,
                      "Ο διάλογος πρέπει να είναι κεντραρισμένος κάθετα");
            
            latch.countDown();
        });
        
        assertTrue(latch.await(5, TimeUnit.SECONDS), "Το test έληξε χρονικά");
    }
    
    @Test
    void testWindowClosing() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        
        SwingUtilities.invokeLater(() -> {
  // Προσομοιώνουμε το κλείσιμο του παραθύρου
            WindowEvent windowClosing = new WindowEvent(dialog, WindowEvent.WINDOW_CLOSING);
            dialog.dispatchEvent(windowClosing);
            
            // Ελέγχουμε αν ο διάλογος δεν είναι πλέον ορατός
            assertFalse(dialog.isVisible(), "Ο διάλογος δεν θα πρέπει να είναι ορατός μετά το κλείσιμο");
            
            latch.countDown();
        });
        
        assertTrue(latch.await(5, TimeUnit.SECONDS), "Το test έληξε χρονικά");
    }
    
    @Test
    void testExceptionHandling() {
        // Προσομοιώνουμε ένα σφάλμα
        Exception testException = new Exception("Test error");
        
        // Ελέγχουμε αν το μήνυμα σφάλματος εμφανίζεται σωστά
        assertDoesNotThrow(() -> {
            SwingUtilities.invokeAndWait(() -> {
                JOptionPane.showMessageDialog(null,
                    "Σφάλμα κατά την εκκίνηση: " + testException.getMessage(),
                    "Σφάλμα",
                    JOptionPane.ERROR_MESSAGE);
            });
        }, "Ο χειρισμός εξαιρέσεων θα πρέπει να λειτουργεί ομαλά");
    }
}