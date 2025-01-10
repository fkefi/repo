package com.cybersolvers.mycvision;

import javax.swing.*;
import java.awt.*;

public class CodeSearchFilter extends JDialog {
    private Filter filter;

    public CodeSearchFilter(Frame parent) {
        super(parent, "Αναζήτηση με Κωδικό", true);
        filter = new Filter();
        requestCode();
    }

    private void requestCode() {
        String input = JOptionPane.showInputDialog(this,
            "Παρακαλώ εισάγετε τον κωδικό σας:",
            "Εισαγωγή Κωδικού",
            JOptionPane.QUESTION_MESSAGE);

        if (input != null && !input.trim().isEmpty()) {
            try {
                double code = Double.parseDouble(input);
                String result = filter.searchByCode(code);
                JOptionPane.showMessageDialog(this, result);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                    "Παρακαλώ εισάγετε έναν έγκυρο αριθμητικό κωδικό.",
                    "Σφάλμα",
                    JOptionPane.ERROR_MESSAGE);
                requestCode(); // Ζητάει ξανά τον κωδικό σε περίπτωση λάθους
            }
        } else {
            dispose(); // Κλείνει το παράθυρο αν ο χρήστης πατήσει ακύρωση
        }
    }
}