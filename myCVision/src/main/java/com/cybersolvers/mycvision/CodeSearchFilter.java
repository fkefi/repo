package com.cybersolvers.mycvision;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;


public class CodeSearchFilter extends JDialog {
    protected Filter filter;

    public CodeSearchFilter(Frame parent) throws SQLException, ClassNotFoundException {
        super(parent, "Search by Code", true);
        filter = new Filter();
        requestCode();
    }

    protected void requestCode() throws SQLException {
        String input = JOptionPane.showInputDialog(this,
            "Please enter your code:",
            "Enter Code",
            JOptionPane.QUESTION_MESSAGE);

        if (input != null && !input.trim().isEmpty()) {
            try {
                double code = Double.parseDouble(input);
                String result = filter.searchByCode(code);
                JOptionPane.showMessageDialog(this, result);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                    "Παρακαλώ εισάγετε έναν έγκυρο αριθμό",
                    "Σφάλμα Εισαγωγής",
                    JOptionPane.ERROR_MESSAGE);
            }
        } else {
            dispose(); 
        }
    }
}
   