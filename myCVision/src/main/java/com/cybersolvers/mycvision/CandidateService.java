package com.cybersolvers.mycvision;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.cybersolvers.mycvision.JsonNode;
import com.cybersolvers.mycvision.ObjectMapper;
import com.cybersolvers.mycvision.SQLiteHandler;
import com.cybersolvers.mycvision.Txtreader;

public class CandidateService  {
    private String[][] id;
    private Map<String, Map<String, Object>> candidates;
    private Map<String, Map<String, Integer>> numbers;
    private double[] weight;
    private int numberOfCandidates;
    private int numberOfCriteria;
    private double[][] points;
    private String jsonFilePath = "E:\\myCVision\\mycv\\src\\resources\\cv\\output.json"; 
    private String jdbcUrl = "C:\\cygwin64\\home\\irenelianou\\repo\\myCVision\\src\\main\\resources"; 

    SQLiteHandler handler = new SQLiteHandler(jdbcUrl);
    Txtreader reader = new Txtreader();

    public CandidateService() throws SQLException {
        this.id = handler.fetchStringArray("ID");
        this.candidates = reader.toMap();
        this.numbers = handler.fetchMapFromDatabase("allTablesData");
        this.weight = handler.fetchDouble1DArray("Weight");
        this.numberOfCandidates = candidates.size();
        this.numberOfCriteria = weight.length;
        this.points = createPoints();
    }

    public double[][] reviewCandidates() throws SQLException {
        double[][] finalCandidates = new double[points.length][2];

        for (int i = 0; i < points.length; i++) {
            double score = calculateScore(i);
            finalCandidates[i][0] = points[i][0];
            finalCandidates[i][1] = score;
        }

        Arrays.sort(finalCandidates, (a, b) -> Double.compare(b[1], a[1]));
        handler.insertDoubleArray("finalCandidates", finalCandidates);
        return finalCandidates;
    }

    private double calculateScore(int i) {
        double score = 0.0;
        for (int j = 1; j < points[i].length; j++) {
            score += points[i][j] * weight[j - 1];
        }
        return score;
    }

    private double[][] createPoints() {
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

    private double[] compareCandidateWithNumbers(Map<String, Object> cand, Map<String, Map<String, Integer>> numbers, double uniqueId) {
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
    