import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CandidateService {

    String[][] id = Database.loadTableData(ID, String.class);
    Map<String, Map<String, Object>> candidates = Database.loadTableData(Candidate, Map<?>.class);
    Map<String, Map<String, Integer>> numbers = Database.loadTableData(allTablesData, Map<?>.class);
    double[] weight = Database.loadTableData(Weight);
    int numberOfCandidates = candidates.size();
    int numberOfCriteria = weight.length;
    double[][] points = createPoints();  // Κλήση της createPoints για να πάρεις τον πίνακα

    public double[][] reviewCandidates() {
        double[][] finalCandidates = new double[points.length][2];

        for (int i = 0; i < points.length; i++) {
            double score = calculateScore(i);
            finalCandidates[i][0] = points[i][0];
            finalCandidates[i][1] = score;
        }

        Arrays.sort(finalCandidates, (a, b) -> Double.compare(b[1], a[1]));
        Database.insertTableData("finalCandidates", finalCandidates, Double.class);
        return finalCandidates;
    } 

    private double calculateScore(int i) {
        double score = 0.0;
        for (int j = 1; j < points[i].length; j++) {
            score += points[i][j] * weight[j - 1];
        }
        return score;
    }

    private double[][] createPoints () {
        double[][] points = new double[numberOfCandidates][weight.length + 1];
        int index = 0;

        // Επεξεργασία κάθε υποψηφίου
        for (Map.Entry<String, Map<String, Object>> entry : candidates.entrySet()) {
            Map<String, Object> cand = entry.getValue();

            // Ανάκτηση του fullName από τον cand
            String fullName = (String) cand.get("fullName");
            double uniqueId = 0.0;

            // Αναζήτηση στο id για το αντίστοιχο ID του fullName
            for (String[] idRow : id) {
                if (idRow[0].equals(fullName)) {
                    uniqueId = Double.parseDouble(idRow[1]); // Μετατροπή ID σε double
                    break;
                }
            }

            // Έλεγχος αν δεν βρέθηκε το ID
            if (uniqueId == 0.0) {
                System.out.println("Warning: No matching ID found for fullName: " + fullName);
            }

            // Κλήση της compareCandidateWithNumbers για τον συγκεκριμένο υποψήφιο
            double[] matchedValues = compareCandidateWithNumbers(cand, numbers, uniqueId);

            // Αποθήκευση του αποτελέσματος στον πίνακα points
            points[index] = matchedValues;

            // Ενημέρωση του δείκτη
            index++;
        }

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
                            System.out.println("Warning: No match found for value " + stringValue + " in category " + category);
                            matchedValuesList.add(0.0); // Αν δεν υπάρχει τιμή, προσθέτουμε 0.0
                        }
                    } else {
                        System.out.println("Warning: No matching category found for " + category);
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
