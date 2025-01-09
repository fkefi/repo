import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

// Κλάση που χειρίζεται τη σύνδεση με τη βάση δεδομένων
class SQLiteHandler {
    private String jdbcUrl;

    public SQLiteHandler(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    // Μέθοδος για αναζήτηση κωδικού με βάση το όνομα χρήστη
    public String getCodeFromName(String fullName) throws SQLException {
        String code = null;
        try (Connection connection = DriverManager.getConnection(jdbcUrl)) {
            String selectCodeQuery = "SELECT code FROM users WHERE full_name = ?";
            try (PreparedStatement stmt = connection.prepareStatement(selectCodeQuery)) {
                stmt.setString(1, fullName);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        code = rs.getString("code");
                    }
                }
            }
        }
        return code;
    }

    // Μέθοδος για αναζήτηση θέσης στην κατάταξη με βάση τον κωδικό
    public int getRankFromCode(String code) throws SQLException {
        int rank = -1;
        try (Connection connection = DriverManager.getConnection(jdbcUrl)) {
            String selectRankQuery = "SELECT rank FROM ranking WHERE code = ?";
            try (PreparedStatement stmt = connection.prepareStatement(selectRankQuery)) {
                stmt.setString(1, code);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        rank = rs.getInt("rank");
                    }
                }
            }
        }
        return rank;
    }
}

public class Filter1 {

    private JFrame frame;
    private JTextField searchField;
    private String jdbcUrl = "jdbc:sqlite:C:\\cygwin64\\home\\irenelianou\\repo\\myCVision\\src\\main\\resources\\database.db"; // Νέα Σύνδεση με SQLite αρχείο βάσης
    private SQLiteHandler dbHandler;  // Αντικείμενο για τη σύνδεση με τη βάση δεδομένων

    public Filter1() {
        // Δημιουργία σύνδεσης με τη βάση δεδομένων
        dbHandler = new SQLiteHandler(jdbcUrl);

        frame = new JFrame("Εφαρμογή Αναζήτησης");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);

        JPanel panel = new JPanel(new BorderLayout());
        searchField = new PlaceholderTextField("Εισάγετε όνομα");
        searchField.setColumns(20);

        JPanel topRightPanel = new JPanel(new BorderLayout());
        topRightPanel.add(searchField, BorderLayout.EAST);
        panel.add(topRightPanel, BorderLayout.NORTH);
        frame.add(panel);

        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String fullName = searchField.getText().trim();

                if (!fullName.isEmpty()) {
                    searchUser(fullName);
                }
            }
        });
    }

    // Μέθοδος αναζήτησης χρήστη
    private void searchUser(String fullName) {
        try {
            // Αναζητούμε τον κωδικό χρήστη
            String code = dbHandler.getCodeFromName(fullName);

            if (code == null) {
                JOptionPane.showMessageDialog(frame, "Δεν βρέθηκε χρήστης με το ονοματεπώνυμο: " + fullName);
            } else {
                // Αναζητούμε την κατάταξη του χρήστη με βάση τον κωδικό
                int rank = dbHandler.getRankFromCode(code);
                if (rank == -1) {
                    JOptionPane.showMessageDialog(frame, "Δεν βρέθηκε θέση στην ταξινόμηση για τον κωδικό: " + code);
                } else {
                    JOptionPane.showMessageDialog(frame, "Κωδικός: " + code + ", Θέση στην ταξινόμηση: " + rank);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Σφάλμα κατά την αναζήτηση στη βάση δεδομένων");
        }
    }

    // Μέθοδος εμφάνισης της εφαρμογής
    public void showApp() {
        frame.setVisible(true);
    }

    // Κλάση για το πεδίο κειμένου με placeholder
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

    // Κλάση main για την εκτέλεση της εφαρμογής
    public static void main(String[] args) {
        Filter filterApp = new Filter();
        filterApp.showApp();
    }
}
