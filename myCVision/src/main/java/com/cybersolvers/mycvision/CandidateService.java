package com.cybersolvers.mycvision;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.*;

import com.cybersolvers.mycvision.JsonNode;
import com.cybersolvers.mycvision.ObjectMapper;

public class CandidateService {
    private String[][] id;
    private Map<String, Map<String, Object>> candidates;
    private Map<String, Map<String, Integer>> numbers;
    private double[] weight;
    private int numberOfCandidates;
    private int numberOfCriteria;
    private double[][] points;

    private String jsonFilePath = "E:\\myCVision\\mycv\\src\\resources\\cv\\output.json"; 
    private String jdbcUrl = "C:\\cygwin64\\home\\irenelianou\\repo\\myCVision\\src\\main\\resources"; 

    SQLiteHandler handler = new SQLiteHandler("C:\\cygwin\\home\\estri\\myrepo\\repo\\myCVision\\src\\main\\resources");

    public CandidateService() {
        this.id = handler.fetchTable("ID");
        this.candidates = handler.fetchJsonAsMap("Candidates");
        this.numbers = handler.fetchJsonAsMap("allTablesData");
        this.weight = handler.fetchTable("Weight");
        this.numberOfCandidates = candidates.size();
        this.numberOfCriteria = weight.length;
        this.points = createPoints();
    }

    public CandidateService(String jsonFilePath, String jdbcUrl) {
        this.jsonFilePath = jsonFilePath;
        this.jdbcUrl = jdbcUrl;
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

    private double[][] createPoints() {
        double[][] points = new double[numberOfCandidates][numberOfCriteria + 1];
        int index = 0;

        for (Map.Entry<String, Map<String, Object>> entry : candidates.entrySet()) {
            Map<String, Object> cand = entry.getValue();
            String fullName = (String) cand.get("fullName");
            double uniqueId = 0.0;

            for (String[] idRow : id) {
                if (idRow[0].equals(fullName)) {
                    uniqueId = Double.parseDouble(idRow[1]);
                    break;
                }
            }

            if (uniqueId == 0.0) {
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
                            System.out.println("Warning: No match found for value " + stringValue + " in category " + category);
                            matchedValuesList.add(0.0);
                        }
                    } else {
                        matchedValuesList.add(0.0);
                    }
                } else if (candValue instanceof Integer) {
                    matchedValuesList.add(((Integer) candValue).doubleValue());
                } else if (category == null && candValue instanceof Double) {
                    matchedValuesList.add((Double) candValue);
                } else {
                    matchedValuesList.add(0.0);
                }
            }
        }

        return matchedValuesList.stream().mapToDouble(Double::doubleValue).toArray();
    }
    public void processCandidates() {
        SQLiteHandler dbHandler = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(new File(jsonFilePath));
    
            dbHandler = new SQLiteHandler(jdbcUrl);
    
            Map<String, String> candidateMap = new HashMap<>();
            for (JsonNode candidateNode : rootNode) { 

                JsonNode fullNameNode = candidateNode.path("fullName");
    
                if (!fullNameNode.isMissingNode()) { 
                    String fullName = fullNameNode.asText();
                    String code = generateRandomCode();
                    candidateMap.put(fullName, code);
                } else {
                    System.err.println("Ο υποψήφιος δεν έχει πεδίο fullName.");
                }
            }
    
            dbHandler.insertJsonAsMap("candidates", candidateMap);
            System.out.println("Τα δεδομένα εισήχθησαν επιτυχώς στη βάση δεδομένων.");
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        } finally {
            if (dbHandler != null) {
                try {
                    dbHandler.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
        public String generateRandomCode() {
            String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
            StringBuilder code = new StringBuilder();
        
            Random random = new Random();
        
        
            for (int i = 0; i < 2; i++) {
                int index = random.nextInt(letters.length());
                code.append(letters.charAt(index));
            }
        
        
            for (int i = 0; i < 4; i++) {
                int digit = random.nextInt(10);
                code.append(digit);
            }
        
            return code.toString();
        
    }
}
    