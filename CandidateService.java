public class CandidateService {

    int[] id = Database.loadTableData(ID);
    double[][] candidate = Database.loadTableData(Candidate);
    double[] weight = Database.loadTableData(Weight);


    public double[][] reviewCandidates() {
        double[][] finalCandidates = new double[candidate.length][2];

        for (int i = 0; i < candidate.length; i++) {
            double score = calculateScore(i);
            finalCandidates[i][0] = (double) id[i];
            finalCandidates[i][1] = score;

        }

        Arrays.sort(finalCandidates, (a, b) -> Double.compare(b[1], a[1]));
        Database.saveTableData(finalCandidates);
        return finalCandidates;
    } 

    private double  calculateScore(int i) {
        double score = 0.0;
         for (int  j = 0; j < candidate[i].length; j++) {
            score += candidate[i][j] * weight[j];
        }
        return score;
    } 

    private double[][] createPoints () {} 
}
