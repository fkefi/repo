import com.cybersolvers.mycvision;
import java.util.*;

public class CandidateService {
    private String[][] id;
    private Map<String, Map<String, Object>> candidates;
    private Map<String, Map<String, Integer>> numbers;
    private double[] weight;
    private int numberOfCandidates;
    private int numberOfCriteria;
    private double[][] points;
    SQLiteHandler handler = new SQLiteHandler("C:\\cygwin\\home\\estri\\myrepo\\repo\\myCVision\\src\\main\\resources");

    public CandidateService() {
    this.id = handler.fetchTable(ID);
    this.candidates = handler.fetchJsonAsMap(Candidates);
    this.numbers = handler.fetchJsonAsMap(allTablesData);
    this.weight = handler.fetchTable(Weight);
    this.numberOfCandidates = candidates.size();
    this.numberOfCriteria = weight.length;
    this.points = createPoints();  // Κλήση της createPoints για να πάρεις τον πίνακα
    }

    public double[][] reviewCandidates() {
        double[][] finalCandidates = new double[points.length][2];

        for (int i = 0; i < points.length; i++) {
            double score = calculateScore(i);
            finalCandidates[i][0] = points[i][0];
            finalCandidates[i][1] = score;
        }

        Arrays.sort(finalCandidates, (a, b) -> Double.compare(b[1], a[1]));
        handler.insertArray("finalCandidates", finalCandidates, finalCandidates.length, 2);
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
        double[][] points = new double[numberOfCandidates][numberOfCriteria + 1];
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

    private double[] compareCandidateWithNumbers(Map<String, Object> cand, Map<String, Map<String, Integer>> numbers, double uniqueId) {
        List<Double> matchedValuesList = new ArrayList<>();


        matchedValuesList.add(uniqueId); // Τοποθετούμε το uniqueId στην πρώτη θέση

        // Πεδίο -> Κατηγορία σύγκρισης
        Map<String, String> fieldToCategory = new LinkedHashMap<>();
            fieldToCategory.put("undergraduateUniversity", "universities");
            fieldToCategory.put("undergraduateDepartment", "bachelorDept");
            fieldToCategory.put("undergraduateGrade", null);     
            fieldToCategory.put("masterUniversity", "universities");
            fieldToCategory.put("masterDepartment", "masterDept");
            fieldToCategory.put("masterGrade", null);
            fieldToCategory.put("phdUniversity", "universities");
            fieldToCategory.put("phdDepartment", "phDDept");
            fieldToCategory.put("phdGrade", null);
            fieldToCategory.put("englishLevel", "levels");
            fieldToCategory.put("frenchLevel", "levels");
            fieldToCategory.put("germanLevel", "levels");
            fieldToCategory.put("spanishLevel", "levels");
            fieldToCategory.put("chineseLevel", "levels");
            fieldToCategory.put("otherLanguageLevel", "levels");
            fieldToCategory.put("workExperienceYears", "workExperience");
            fieldToCategory.put("officeSkills", "levels");
            fieldToCategory.put("programmingLanguage", "yesNo");

        for (Map.Entry<String, String> entry : fieldToCategory.entrySet()) {
            String field = entry.getKey();
            String category = entry.getValue();

            if (cand.containsKey(field)) {
                Object candValue = cand.get(field);
                if (candValue instanceof String) {
                    String stringValue = (String) candValue;
                    if (category != null && numbers.containsKey(category)) {
                        Map<String, Integer> categoryData = numbers.get(category);
                        if (categoryData.containsKey(stringValue)) {
                            matchedValuesList.add((double) categoryData.get(stringValue));
                        } else {
                            System.out.println("Warning: No match found for value " + stringValue + " in category " + category);
                            matchedValuesList.add(0.0);
                        }
                    } else {
                        matchedValuesList.add(0.0);
                    }
                } else if (candValue instanceof Integer) {
                    matchedValuesList.add(((Integer) candValue).doubleValue());
                } else if (category == null && candValue instanceof Double) {
                    // Εισαγωγή βαθμών (grades) όταν η κατηγορία είναι null και η τιμή είναι double
                    matchedValuesList.add((Double) candValue);
                } else {
                    matchedValuesList.add(0.0);
                }
            }
            
        }

        // Μετατροπή λίστας σε πίνακα double[]
        double[] matchedValues = matchedValuesList.stream().mapToDouble(Double::doubleValue).toArray();

        return matchedValues;
    }

}
