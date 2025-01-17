package com.cybersolvers.mycvision;
import java.net.URL;
import java.sql.*;
import java.util.*;

import com.cybersolvers.mycvision.SQLiteHandler;

public class CandidateService  {
    protected String[][] id;
    protected Map<String, Map<String, Object>> candidates;
    protected Map<String, Map<String, Integer>> numbers;
    protected double[] weight;
    protected int numberOfCandidates;
    protected int numberOfCriteria;
    protected double[][] points;
    protected String jsonFilePath = "E:\\myCVision\\mycv\\src\\resources\\cv\\output.json"; 


    protected final SQLiteHandler handler;
    Txtreader reader;

    public CandidateService() throws SQLException, ClassNotFoundException {
        // Φορτώνουμε τον driver
        Class.forName("org.sqlite.JDBC");

        // Δημιουργούμε το connection string
        String dbUrl = "jdbc:sqlite::resource:my_database.db";
        this.handler = new SQLiteHandler(dbUrl);
        this.reader = new Txtreader();
        this.id = handler.fetchStringArray("id");
        reader.processFiles();
        this.candidates = reader.allCandidates;
        this.numbers = handler.fetchMapFromDatabase("allTablesData");
        this.weight = handler.fetchDouble1DArray("Weight");
        this.numberOfCandidates = candidates.size();
        this.numberOfCriteria = weight.length;
        this.points = createPoints();
        //System.out.println("CandidateService initialized:" + candidates + numberOfCandidates);
    }

    public CandidateService(String[][] id, Map<String, Map<String, Object>> candidates,
     Map<String, Map<String, Integer>> numbers, double[] weight) {
        this.id = id;
        this.candidates = candidates;
        this.numbers = numbers;
        this.weight = weight;
        this.numberOfCandidates = candidates.size();
        this.numberOfCriteria = weight.length;
        this.points = createPoints();
        this.handler = null;
    }

    public double[][] reviewCandidates() throws SQLException {
        double[][] finalCandidates = new double[points.length][2];

        for (int i = 0; i < points.length; i++) {
            double score = calculateScore(i);
            finalCandidates[i][0] = points[i][0];
            finalCandidates[i][1] = score;
        }

        Arrays.sort(finalCandidates, (a, b) -> Double.compare(b[1], a[1]));
        if (handler != null) {
            handler.insertDoubleArray("finalCandidates", finalCandidates);
        }
            
       
        return finalCandidates;
    }

    protected double calculateScore(int i) {
        double score = 0.0;
        for (int j = 1; j < points[i].length; j++) {
            //System.out.println("Point[" + j + "]: " + points[i][j] + ", Weight[" + (j-1) + "]: " + weight[j-1]);
            score += points[i][j] * weight[j - 1];
        }
        return score;
    }

    protected double[][] createPoints() {
        double[][] points = new double[numberOfCandidates][numberOfCriteria + 1];
        int index = 0;

        for (Map.Entry<String, Map<String, Object>> entry : candidates.entrySet()) {
            Map<String, Object> cand = entry.getValue();
            String fullName = (String) cand.get("fullName");
            double uniqueId = -99.0;

            for (String[] idRow : id) {
                if (idRow[0].equals(fullName)) {
                    uniqueId = Double.parseDouble(idRow[1]);
                    break;
                }
            }

            if (uniqueId == -99.0) {
                System.out.println("Warning: No matching ID found for fullName: " + fullName);
            }

            double[] matchedValues = compareCandidateWithNumbers(cand, numbers, uniqueId);
            points[index] = matchedValues;
            index++;
        }

        return points;
    }

    protected double[] compareCandidateWithNumbers(Map<String, Object> cand, Map<String, Map<String, Integer>> numbers, double uniqueId) {
        List<Double> matchedValuesList = new ArrayList<>();
        matchedValuesList.add(uniqueId);

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
                        } else if ((field.equals("undergraduateUniversity") || field.equals("undergraduateDepartment") ||
                                field.equals("masterUniversity") || field.equals("masterDepartment") ||
                                field.equals("phdUniversity") || field.equals("phdDepartment")) &&
                                !stringValue.equalsIgnoreCase("no")) {
                            matchedValuesList.add(1.0);
                        } else {
                            matchedValuesList.add(0.0);
                        }
                    } else {
                        matchedValuesList.add(0.0);
                    }
                } else if (candValue instanceof Integer) {
                    matchedValuesList.add(((Integer) candValue).doubleValue());
                } else if (category == null && candValue instanceof Double) {
                   double grade = (Double) candValue;
                if (grade >= 10 ||  (grade >= 1 && grade <= 4) ) {
                    System.out.println("Warning: Grade in field " + field + " for candidate " + cand.get("fullName") + " is " + grade + "(setting to 0)");
                    grade = 0.0;
                }
                matchedValuesList.add(grade);
                    
                } else {
                    matchedValuesList.add(0.0);
                } 
            } else {
                String fullName = (String) cand.getOrDefault("fullName", "Unknown");
                System.out.println("Warning: No value found for field '" + field + "' in candidate '" + fullName + "'");
                matchedValuesList.add(0.0);
            }
        }

        return matchedValuesList.stream().mapToDouble(Double::doubleValue).toArray();
        
    }

}
    