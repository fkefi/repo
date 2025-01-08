import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class CandidateRepository {

    public static void main(String[] args) {
        // Δημιουργία του παραθύρου
        JFrame frame = new JFrame("Εφαρμογή Αναζήτησης");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);

        // Δημιουργία του κύριου πάνελ με BorderLayout
        JPanel panel = new JPanel(new BorderLayout());

        // Δημιουργία του πεδίου κειμένου για την αναζήτηση με placeholder
        PlaceholderTextField searchField = new PlaceholderTextField("Εισάγετε όνομα");
        searchField.setColumns(20); // Ορισμός πλάτους του πεδίου κειμένου

        // Δημιουργία του πάνελ για την πάνω δεξιά γωνία
        JPanel topRightPanel = new JPanel(new BorderLayout());
        topRightPanel.add(searchField, BorderLayout.EAST);

        // Προσθήκη του πάνελ στην πάνω δεξιά γωνία του κύριου πάνελ
        panel.add(topRightPanel, BorderLayout.NORTH);

        // Προσθήκη του κύριου πάνελ στο παράθυρο
        frame.add(panel);

        // Σύνδεση με τη βάση δεδομένων
        String jdbcUrl = "jdbc:mysql://localhost:3306/your_database"; // Αντικαταστήστε με το URL της βάσης δεδομένων σας
        String username = "your_username"; // Αντικαταστήστε με το όνομα χρήστη της βάσης δεδομένων σας
        String password = "your_password"; // Αντικαταστήστε με τον κωδικό πρόσβασης της βάσης δεδομένων σας

        // Προσθήκη ακροατή για την παρακολούθηση αλλαγών στο πεδίο κειμένου
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String fullName = searchField.getText().trim();

                if (!fullName.isEmpty()) {
                    try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
                        // Εύρεση του κωδικού χρήστη από το ονοματεπώνυμο
                        String code = null;
                        String selectCodeQuery = "SELECT code FROM users WHERE full_name = ?";
                        try (PreparedStatement selectCodeStmt = connection.prepareStatement(selectCodeQuery)) {
                            selectCodeStmt.setString(1, fullName);
                            try (ResultSet rs = selectCodeStmt.executeQuery()) {
                                if (rs.next()) {
                                    code = rs.getString("code");
                                }
                            }
                        }

                        if (code == null) {
                            JOptionPane.showMessageDialog(frame, "Δεν βρέθηκε χρήστης με το ονοματεπώνυμο: " + fullName);
                        } else {
                            // Εύρεση της θέσης στην ταξινόμηση
                            int rank = -1;
                            String selectRankQuery = "SELECT rank FROM ranking WHERE code = ?";
                            try (PreparedStatement selectRankStmt = connection.prepareStatement(selectRankQuery)) {
                                selectRankStmt.setString(1, code);
                                try (ResultSet rs = selectRankStmt.executeQuery()) {
                                    if (rs.next()) {
                                        rank = rs.getInt("rank");
                                    }
                                }
                            }

                            if (rank == -1) {
                                JOptionPane.showMessageDialog(frame, "Δεν βρέθηκε θέση στην ταξινόμηση για τον κωδικό: " + code);
                            } else {
                                JOptionPane.showMessageDialog(frame, "Κωδικός: " + code + ", Θέση στην ταξινόμηση: " + rank);
                            }
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        // Εμφάνιση του παραθύρου
        frame.setVisible(true);
    }

    // Προσαρμοσμένη κλάση JTextField με υποστήριξη placeholder
    static class PlaceholderTextField extends JTextField {
        private String placeholder;

        public PlaceholderTextField(String placeholder) {
            this.placeholder = placeholder;
            setText(placeholder);
            setForeground(Color.GRAY);
            addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    if (getText().equals(placeholder)) {
                        setText("");
                        setForeground(Color.BLACK);
                    }
                }

                @Override
                public void focusLost(FocusEvent e) {
                    if (getText().isEmpty()) {
                        setText(placeholder);
                        setForeground(Color.GRAY);
                    }
                }
            });
        }

        @Override
        public String getText() {
            String text = super.getText();
            return text.equals(placeholder) ? "" : text;
        }
    }
}