package com.cybersolvers.mycvision;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FilterTest {
    private Filter filter;

    @BeforeEach
    void setUp() {
        filter = new Filter();
    }

    @Test
    void testGenerateRandomCode() {
        // Έλεγχος ότι παράγονται διαφορετικοί κωδικοί
        String code1 = filter.generateRandomCode();
        String code2 = filter.generateRandomCode();

        // Επαλήθευση ότι οι κωδικοί είναι διαφορετικοί
        assertNotEquals(code1, code2);

        // Επαλήθευση ότι οι κωδικοί είναι αριθμητικοί
        assertTrue(code1.matches("\\d+"));
        assertTrue(code2.matches("\\d+"));

        // Επαλήθευση ότι το μήκος είναι λογικό (μέχρι 6 ψηφία)
        assertTrue(code1.length() <= 6);
        assertTrue(code2.length() <= 6);
    }
    @Test
    void testSearchByCodeWithValidInput() {
        // Έλεγχος με έγκυρο κωδικό
        String result = filter.searchByCode(123456);
        assertNotNull(result, "Το αποτέλεσμα δεν πρέπει να είναι null");
        
        // Το αποτέλεσμα πρέπει να είναι ένα από τα αναμενόμενα
        assertTrue(
            result.equals("Code not found.") || 
            result.startsWith("Name:") ||
            result.equals("Error during search"),
            "Μη αναμενόμενο αποτέλεσμα: " + result
        );
    }

    @Test
    void testSearchByCodeWithLargeNumber() {
        // Έλεγχος με πολύ μεγάλο αριθμό
        String result = filter.searchByCode(9999999);
        assertNotNull(result, "Το αποτέλεσμα δεν πρέπει να είναι null");
        
        // Το αποτέλεσμα πρέπει να είναι ένα από τα αναμενόμενα
        assertTrue(
            result.equals("Code not found.") || 
            result.equals("Error during search"),
            "Μη αναμενόμενο αποτέλεσμα: " + result
        );
    }
    @Test
    void testIdArrayNotNull() {
        try {
            // Έλεγχος ότι ο πίνακας id έχει δημιουργηθεί
            java.lang.reflect.Field idField = Filter.class.getDeclaredField("id");
            idField.setAccessible(true);
            String[][] id = (String[][]) idField.get(filter);
            
            assertNotNull(id, "Ο πίνακας id δεν πρέπει να είναι null");
            
            // Έλεγχος ότι κάθε εγγραφή έχει σωστή μορφή
            for (String[] record : id) {
                assertNotNull(record, "Κάθε εγγραφή δεν πρέπει να είναι null");
                assertEquals(2, record.length, "Κάθε εγγραφή πρέπει να έχει 2 στοιχεία");
                assertNotNull(record[0], "Το όνομα δεν πρέπει να είναι null");
                assertNotNull(record[1], "Ο κωδικός δεν πρέπει να είναι null");
                assertTrue(record[1].matches("\\d+"), "Ο κωδικός πρέπει να είναι αριθμητικός");
            }
            
        } catch (Exception e) {
            fail("Σφάλμα κατά τον έλεγχο του πίνακα id: " + e.getMessage());
        }
    }
}