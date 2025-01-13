package com.cybersolvers.mycvision.*;

import com.cybersolvers.mycvision.*;
import java.sql.SQLException;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class App 
{
    public static void main( String[] args ) throws SQLException
    {

        // Κλήση μεθόδου για γραφικά 
        //Κλήση μεθόδου για txt parsing

        // Κλήση μεθοδού CandidateService για την δημηιουργία τελικού πίνακα και ταξινόμιση υποψηφίων
        CandidateService service = new CandidateService();
        double[][] results = service.reviewCandidates();

        System.out.println("Reviewed Candidates:");
        for (double[] candidate : results) {
            System.out.println("Candidate ID: " + candidate[0] + ", Score: " + candidate[1]);
        }
        Scanner scanner = new Scanner(System.in);
        SwingUtilities.invokeLater(() -> {
            try {
                JFrame frame = new JFrame();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                
                CodeSearchFilter dialog = new CodeSearchFilter(frame);
                dialog.setLocationRelativeTo(null); 
                
                // Όταν κλείσει ο διάλογος, τερματίζουμε την εφαρμογή
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosed(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                
                dialog.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null,
                    "Σφάλμα κατά την εκκίνηση: " + e.getMessage(),
                    "Σφάλμα",
                    JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        });
        // Κλήση μεθόδου MailerService για αποστολή των αποτελεσμάτων στην εταιρία
        System.out.print("Enter recipient email: ");
        String recipientEmail = scanner.nextLine(); // Εισαγωγή email από τον χρήστη

        MailerService mailerService = new MailerService();
        mailerService.sendEmail("recipientEmail");
        
    }
}
