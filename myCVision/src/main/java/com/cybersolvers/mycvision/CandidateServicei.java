package com.cybersolvers.mycvision;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.Random;
public class CandidateService {
    public static void main(String[] args) {
        String jsonFilePath = ""; // Αντικαταστήστε με τη διαδρομή του αρχείου JSON
        String jdbcUrl = ""; // Αντικαταστήστε με το URL της βάσης δεδομένων σας
        try {
            // Ανάγνωση του JSON αρχείου
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(new File(jsonFilePath));
            JsonNode namesNode = rootNode.get("names"); // Αντικαταστήστε με το όνομα του πεδίου που περιέχει τα ονοματεπώνυμα

            // Σύνδεση με τη βάση δεδομένων
            Connection connection = DriverManager.getConnection(jdbcUrl);

            // Προετοιμασία του SQL statement για εισαγωγή δεδομένων
            String sql = "INSERT INTO StringID (full_name, code) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            // Επεξεργασία κάθε ονοματεπωνύμου
            for (JsonNode nameNode : namesNode) {
                String fullName = nameNode.asText();
                String code = generateRandomCode();

                // Εισαγωγή δεδομένων στη βάση
                preparedStatement.setString(1, fullName);
                preparedStatement.setString(2, code);
                preparedStatement.executeUpdate();
            }

            // Κλείσιμο της σύνδεσης
            preparedStatement.close();
            connection.close();

            System.out.println("Τα δεδομένα εισήχθησαν επιτυχώς στη βάση δεδομένων.");

        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    // Μέθοδος για τη δημιουργία τυχαίου κωδικού της μορφής "DB1234"
    private static String generateRandomCode() {
        String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder code = new StringBuilder();

        Random random = new Random();

        // Προσθήκη δύο τυχαίων κεφαλαίων γραμμάτων
        for (int i = 0; i < 2; i++) {
            int index = random.nextInt(letters.length());
            code.append(letters.charAt(index));
        }

        // Προσθήκη τεσσάρων τυχαίων αριθμών
        for (int i = 0; i < 4; i++) {
            int digit = random.nextInt(10);
            code.append(digit);
        }

        return code.toString();
    }
}
