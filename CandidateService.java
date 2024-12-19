import java.util.Arrays;
import java.util.Map;

public class CandidateService {

    String[][] id = Database.loadTableData(ID, String.class);
    Map<> candidate = Database.loadTableData(Candidate, Map<?>.class);
    Map<String, Map<String, Integer>> numbers = Database.loadTableData(allTablesData, Map<?>.class)
    double[] weight = Database.loadTableData(Weight);
    double[][] points;

    public record Candidate(
    String fullName,
    String undergraduateDegree,
    String undergraduateUniversity,
    String undergraduateDepartment,
    int undergraduateGrade,
    String masterDegree,
    String masterUniversity,
    String masterDepartment,
    int masterGrade,
    String phdDegree,
    String phdUniversity,
    String phdDepartment,
    int phdGrade,
    String englishLevel,
    String frenchLevel,
    String germanLevel,
    String spanishLevel,
    String chineseLevel,
    String otherLanguageLevel,
    int workExperienceYears,
    String officeSkills,
    String programmingLanguage
    ) {}



    public double[][] reviewCandidates() {
        double[][] finalCandidates = new double[points.length][2];

        for (int i = 0; i < points.length; i++) {
            double score = calculateScore(i);
            finalCandidates[i][0] = Double.parseDouble(id[i]);
            finalCandidates[i][1] = score;

        }

        Arrays.sort(finalCandidates, (a, b) -> Double.compare(b[1], a[1]));
        Database.insertTableData("finalCandidates", finalCandidates, Double.class);
        return finalCandidates;
    } 

    private double calculateScore(int i) {
        double score = 0.0;
         for (int  j = 0; j < points[i].length; j++) {
            score += points[i][j] * weight[j];
        }
        return score;
    } 

    private double[][] createPoints () {

        return points;
    } 
}
