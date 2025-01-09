package com.cybersolvers.mycvision;
import com.sendgrid.*;
import java.io.IOException;
import java.sql.*;


public class MailerService {

    // Φορτώνουμε το API Key 
private static final String SENDGRID_API_KEY = "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";


    private SQLiteHandler sqliteHandler; // Αναφορά στην κλάση για την βάση δεδομένων

    // Κατασκευαστής της κλάσης για να περάσουμε τη σύνδεση με τη βάση δεδομένων
    public MailerService(SQLiteHandler sqliteHandler) {
        this.sqliteHandler = sqliteHandler;
    }

    // Μέθοδος για να στείλουμε το email με τα finalCandidates από τη βάση δεδομένων
    public void sendEmail(String recipientEmail) {
        double[][] finalCandidates = getFinalCandidatesFromDB();

        if (finalCandidates != null) {
            try {
                // Δημιουργία του αντικειμένου SendGrid API
                SendGrid sendGrid = new SendGrid(SENDGRID_API_KEY);
                Request request = new Request();

                // Δημιουργία του περιεχομένου του email
                StringBuilder emailContent = new StringBuilder();
                emailContent.append("Αγαπητέ παραλήπτη,");
                emailContent.append("Επισυνάπτεται ο πίνακας με τα τελικά αποτελέσματα:\n\n");

                // Επεξεργασία του πίνακα finalCandidates και προσθήκη στο περιεχόμενο του email
                for (int i = 0; i < finalCandidates.length; i++) {
                    for (int j = 0; j < finalCandidates[i].length; j++) {
                        emailContent.append(finalCandidates[i][j] + "\t");
                    }
                    emailContent.append("\n");
                }

                // Δημιουργία του email
                Mail mail = new Mail(
                        new Email("t8230156@aueb.gr"), // Από ποιον στέλνεται το email
                        "Αποτελέσματα FinalCandidates", // Θέμα του email
                        new Email(recipientEmail), // Παραλήπτης
                        new Content("text/plain", emailContent.toString()) // Περιεχόμενο
                );

                // Ρύθμιση της αίτησης για αποστολή
                request.setMethod(Method.POST);
                request.setEndpoint("mail/send");
                request.setBody(mail.build());

                // Αποστολή του email
                Response response = sendGrid.api(request);
                System.out.println("Status Code: " + response.getStatusCode());
                System.out.println("Response Body: " + response.getBody());
                System.out.println("Response Headers: " + response.getHeaders());
            } catch (IOException ex) {
                System.err.println("Error sending email: " + ex.getMessage());
            }
        } else {
            System.out.println("No data available for finalCandidates.");
        }
    }

    // Μέθοδος για να πάρουμε τα finalCandidates από τη βάση δεδομένων
    private double[][] getFinalCandidatesFromDB() {
        double[][] finalCandidates = null;
        String query = "SELECT * FROM finalCandidates"; // Αντικατέστησε με την κατάλληλη SQL query

        try (Connection conn = sqliteHandler.getConnection(); Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);

            // Υπολογισμός του αριθμού των γραμμών και των στηλών
            rs.last();
            int rowCount = rs.getRow();
            rs.beforeFirst();
            int columnCount = rs.getMetaData().getColumnCount();

            // Δημιουργία του πίνακα finalCandidates με τις διαστάσεις (rowCount x columnCount)
            finalCandidates = new double[rowCount][columnCount];

            // Ανάγνωση των δεδομένων από το ResultSet και αποθήκευσή τους στον πίνακα
            int row = 0;
            while (rs.next()) {
                for (int col = 1; col <= columnCount; col++) {
                    finalCandidates[row][col - 1] = rs.getDouble(col);
                }
                row++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return finalCandidates;
    }

    public static void main(String[] args) {
        // Φόρτωση του sqliteHandler και σύνδεση με την βάση
        SQLiteHandler sqliteHandler = new SQLiteHandler("C:\\cygwin64\\home\\efifk\\repo\\myCVision\\src\\main\\resources");

        // Δημιουργία αντικειμένου MailerService και αποστολή του email
        MailerService mailerService = new MailerService(sqliteHandler);
        mailerService.sendEmail("recipient_email@example.com");
    }
}
