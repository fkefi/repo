import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CandidateService {

    String[][] id = Database.loadTableData(ID, String.class);
    Map<String,Map<String, Object>> candidate = Database.loadTableData(Candidate, Map<?>.class);
    Map<String, Map<String, Integer>> numbers = Database.loadTableData(allTablesData, Map<?>.class);
    double[] weight = Database.loadTableData(Weight);
    double[][] points = new double[candidate.size()][weight.length];

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

    public static double[] compareCandidateWithNumbers(Map<String, Object> cand, Map<String, Map<String, Integer>> numbers, double uniqueId) {
        List<Double> matchedValuesList = new ArrayList<>();
    
        // Αποθήκευση του fullName ως μοναδικό ID στην πρώτη θέση του πίνακα
        matchedValuesList.add(uniqueId); // Τοποθετούμε το uniqueId στην πρώτη θέση
    
        // Πεδίο -> Κατηγορία σύγκρισης
        Map<String, String> fieldToCategory = Map.of(
            "undergraduateUniversity", "universities",
            "masterUniversity", "universities",
            "phdUniversity", "universities",
            "undergraduateDepartment", "bachelorDept",
            "masterDepartment", "masterDept",
            "phdDepartment", "phDDept",
            "englishLevel", "levels",
            "frenchLevel", "levels",
            "germanLevel", "levels",
            "spanishLevel", "levels",
            "chineseLevel", "levels",
            "otherLanguageLevel", "levels",
            "officeSkills", "levels",
            "workExperienceYears", "workExperience",
            "programmingLanguage", "yesNo"
        );
    
        // Διατρέχουμε όλα τα πεδία προς σύγκριση
        for (Map.Entry<String, String> entry : fieldToCategory.entrySet()) {
            String field = entry.getKey();
            String category = entry.getValue();
    
            if (cand.containsKey(field)) {
                Object candValue = cand.get(field);
                if (candValue instanceof String) {
                    String stringValue = (String) candValue;
                    if (numbers.containsKey(category)) {
                        Map<String, Integer> categoryData = numbers.get(category);
                        if (categoryData.containsKey(stringValue)) {
                            matchedValuesList.add((double) categoryData.get(stringValue));
                        } else {
                            matchedValuesList.add(0.0); // Αν δεν υπάρχει τιμή, προσθέτουμε 0.0
                        }
                    } else {
                        matchedValuesList.add(0.0); // Αν δεν υπάρχει κατηγορία, προσθέτουμε 0.0
                    }
                } else if (candValue instanceof Integer) {
                    // Εισάγουμε την τιμή όπως είναι (για τα int πεδία)
                    matchedValuesList.add(((Integer) candValue).doubleValue());
                } else {
                    matchedValuesList.add(0.0); // Μηδενική τιμή για άγνωστους τύπους
                }
            } else {
                // Αν το πεδίο δεν υπάρχει και είναι String, προσθέτουμε 0.0
                matchedValuesList.add(0.0);
            }
        }
    
        // Μετατροπή λίστας σε πίνακα double[]
        double[] matchedValues = matchedValuesList.stream().mapToDouble(Double::doubleValue).toArray();
    
        return matchedValues;
    }
    
}
