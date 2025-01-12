package com.cybersolvers.mycvision.*;

import com.cybersolvers.mycvision.*;
import java.sql.SQLException;
import java.util.Scanner;

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

        // Κλήση μεθόδου MailerService για αποστολή των αποτελεσμάτων στην εταιρία
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("Enter recipient email: ");
        String recipientEmail = scanner.nextLine(); // Εισαγωγή email από τον χρήστη

        MailerService mailerService = new MailerService();
        mailerService.sendEmail("recipientEmail");
        
    }
}
