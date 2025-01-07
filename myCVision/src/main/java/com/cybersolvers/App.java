package com.cybersolvers;

public class App 
{
    public static void main( String[] args )
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
        
    }
}
