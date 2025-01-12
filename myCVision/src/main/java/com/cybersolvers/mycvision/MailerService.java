package com.cybersolvers.mycvision;

import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Content;

import java.io.IOException;
import java.sql.*;

public class MailerService {
    double[][] finalCandidates;

    // Φορτώνουμε το API Key 
    private static final String SENDGRID_API_KEY = "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";

    // Μέθοδος για να στείλουμε το email με τα finalCandidates από τη βάση δεδομένων
    public void sendEmail(String recipientEmail) throws SQLException {
        // Εδώ χρησιμοποιούμε την μέθοδο fetchDoubleArray από την κλάση SQLiteHandler για να πάρουμε τα δεδομένα
        String dbUrl = "C:\\cygwin64\\home\\efifk\\repo\\myCVision\\src\\main\\resources";
        SQLiteHandler sqliteHandler = new SQLiteHandler(dbUrl);
        String tableName = "finalCandidates";  // Όνομα του πίνακα
        this.finalCandidates = sqliteHandler.fetchDoubleArray(tableName);  // Φορτώνουμε τα δεδομένα

        if (finalCandidates != null) {
            try {
                // Δημιουργία του αντικειμένου SendGrid API
                SendGrid sendGrid = new SendGrid(SENDGRID_API_KEY);
                Request request = new Request();

                // Δημιουργία του περιεχομένου του email
                StringBuilder emailContent = new StringBuilder();
                emailContent.append("Αγαπητέ παραλήπτη,\n");
                emailContent.append("Επισυνάπτεται ο πίνακας με τα τελικά αποτελέσματα:\n\n");

                // Επεξεργασία του πίνακα finalCandidates και προσθήκη στο περιεχόμενο του email
                for (int i = 0; i < finalCandidates.length; i++) {
                    for (int j = 0; j < finalCandidates[i].length; j++) {
                        emailContent.append(finalCandidates[i][j] + "\t");
                    }
                    emailContent.append("\n");
                }

                // Δημιουργία του email
                Email from = new Email("t8230156@aueb.gr");
                String subject = "Αποτελέσματα FinalCandidates";
                Email to = new Email(recipientEmail);
                
                Content content = new Content();
                content.setType("text/plain");
                content.setValue(emailContent.toString());
                
                Mail mail = new Mail(from, subject, to, content);

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

}