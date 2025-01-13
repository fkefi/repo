package com.cybersolvers.mycvision;


import com.cybersolvers.mycvision.*;
import java.sql.SQLException;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class App 
{
    public static void main( String[] args ) throws SQLException
    {

        // Κλήση μεθόδου για γραφικά 
        CustomCriteriaApp customCriteriaApp = new CustomCriteriaApp();
        JFrame guiFrame =customCriteriaApp.createAndShowGUI(); // Assuming it returns a JFrame

        // Use a lock object to wait until the GUI is closed
        final Object lock = new Object();

        // Add a WindowListener to the JFrame to detect when it closes
        guiFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                synchronized (lock) {
                    lock.notify(); // Notify the waiting thread that the GUI has closed
                }
            }
        });

        // Wait until the GUI is closed
        synchronized (lock) {
            try {
                lock.wait(); // Wait until the GUI is closed
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //Κλήση μεθόδου για κατάθεση βιογραφικών
        JFrame cvGuiFrame = CVSubmissionApp2.startCVSubmissionApp();

        cvGuiFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                synchronized (lock) {
                    lock.notify(); // Ειδοποιεί την κύρια ροή ότι το παράθυρο της CVSubmissionApp έχει κλείσει
                }
            }
        });

        // Περιμένει μέχρι να κλείσει το παράθυρο της CVSubmissionApp
        synchronized (lock) {
            try {
                lock.wait(); // Περιμένει το κλείσιμο του παραθύρου της CVSubmissionApp
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


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
