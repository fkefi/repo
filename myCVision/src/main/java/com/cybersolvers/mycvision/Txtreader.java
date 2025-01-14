package com.cybersolvers.mycvision;
import java.io.BufferedWriter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Txtreader {

    public Txtreader() {

    }

    private String fullName;
    private String undergraduateUniversity;
    private String undergraduateDepartment;
    private double undergraduateGrade;
    private String masterUniversity;
    private String masterDepartment;
    private double masterGrade;
    private String phdUniversity;
    private String phdDepartment;
    private double phdGrade;
    private String englishLevel;
    private String frenchLevel;
    private String germanLevel;
    private String spanishLevel;
    private String chineseLevel;
    private String otherLanguageLevel;
    private String workExperienceYears;
    private String officeSkills;
    private String programmingLanguage;
    private static int candidateCounter = 0;
    Map<String, Map<String, Object>> allCandidates = new LinkedHashMap<>();

    public void processFiles() {
        //Map<String, Map<String, Object>> allCandidates = new LinkedHashMap<>();
        Path directoryPath = CVSubmissionApp.cvFolder;
        String outputFilePath = "E:\\myCVision\\mycv\\src\\resources\\cv\\output.json";
        
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        
        try (BufferedWriter writer = Files.newBufferedWriter(Path.of(outputFilePath))) {
            List<Path> files = Files.list(directoryPath)
                    .filter(filePath -> filePath.toString().endsWith(".txt"))
                    .collect(Collectors.toList());

            for (Path filePath : files) {
                try {
                    clearData();

                    List<String> lines = Files.readAllLines(filePath);

                    for (String line : lines) {
                        if (!line.contains(":")) continue;

                        String[] parts = line.split(":", 2);
                        if (parts.length < 2) continue;

                        String key = parts[0].trim();
                        String value = parts[1].trim();

                        switch (key) {
                            case "Full Name":
                                this.fullName = value;
                                break;
                            case "University (Name/No)":
                                if (this.undergraduateUniversity == null) {
                                    this.undergraduateUniversity = value;
                                } else if (this.masterUniversity == null) {
                                    this.masterUniversity = value;
                                } else {
                                    this.phdUniversity = value;
                                }
                                break;
                            case "Department":
                                if (this.undergraduateDepartment == null) {
                                    this.undergraduateDepartment = value;
                                } else if (this.masterDepartment == null) {
                                    this.masterDepartment = value;
                                } else {
                                    this.phdDepartment = value;
                                }
                                break;
                            case "Undergraduate Grade":
                                this.undergraduateGrade = Double.parseDouble(value);
                                break;
                            case "Masters University (Name/No)":
                                this.masterUniversity = value;
                                break;
                            case "Masters Department":
                                this.masterDepartment = value;
                                break;
                            case "Masters Grade":
                                this.masterGrade = Double.parseDouble(value);
                                break;
                            case "PhD University (Name/No)":
                                this.phdUniversity = value;
                                break;
                            case "PhD Department":
                                this.phdDepartment = value;
                                break;
                            case "PhD Grade":
                                this.phdGrade = Double.parseDouble(value);
                                break;
                            case "English (Excellent/Very Good/Good/No)":
                                this.englishLevel = value;
                                break;
                            case "French (Excellent/Very Good/Good/No)":
                                this.frenchLevel = value;
                                break;
                            case "German (Excellent/Very Good/Good/No)":
                                this.germanLevel = value;
                                break;
                            case "Spanish (Excellent/Very Good/Good/No)":
                                this.spanishLevel = value;
                                break;
                            case "Chinese (Excellent/Very Good/Good/No)":
                                this.chineseLevel = value;
                                break;
                            case "Other Language Level (Excellent/Very Good/Good/No)":
                                this.otherLanguageLevel = value;
                                break;
                            case "Years of Experience":
                                this.workExperienceYears = value;
                                break;
                            case "Office Skills (Excellent/Very Good/Good/No)":
                                this.officeSkills = value;
                                break;
                            case "Programming Skills (Yes/No)":
                                this.programmingLanguage = value;
                                break;    
                        }
                    }

                    Map<String, Map<String, Object>> candidateData = toMap();
                    allCandidates.putAll(candidateData);
                    
                    Files.deleteIfExists(filePath);
                    
                } catch (IOException | NumberFormatException e) {
                    e.printStackTrace();
                }
            }

            String jsonData = gson.toJson(allCandidates);
            writer.write(jsonData);
            writer.newLine();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void clearData() {
        fullName = null;
        undergraduateUniversity = null;
        undergraduateDepartment = null;
        undergraduateGrade = 0.0;
        masterUniversity = null;
        masterDepartment = null;
        masterGrade = 0.0;
        phdUniversity = null;
        phdDepartment = null;
        phdGrade = 0.0;
        englishLevel = null;
        frenchLevel = null;
        germanLevel = null;
        spanishLevel = null;
        chineseLevel = null;
        otherLanguageLevel = null;
        workExperienceYears = null;
        officeSkills = null;
        programmingLanguage= null;
    }

    protected Map<String, Map<String, Object>> toMap() {
        Map<String, Map<String, Object>> candidates = new LinkedHashMap<>();
        Map<String, Object> candidateData = new LinkedHashMap<>();
        
        candidateData.put("fullName", fullName);
        candidateData.put("undergraduateUniversity", undergraduateUniversity);
        candidateData.put("undergraduateDepartment", undergraduateDepartment);
        candidateData.put("undergraduateGrade", undergraduateGrade);
        candidateData.put("masterUniversity", masterUniversity);
        candidateData.put("masterDepartment", masterDepartment);
        candidateData.put("masterGrade", masterGrade);
        candidateData.put("phdUniversity", phdUniversity);
        candidateData.put("phdDepartment", phdDepartment);
        candidateData.put("phdGrade", phdGrade);
        candidateData.put("englishLevel", englishLevel);
        candidateData.put("frenchLevel", frenchLevel);
        candidateData.put("germanLevel", germanLevel);
        candidateData.put("spanishLevel", spanishLevel);
        candidateData.put("chineseLevel", chineseLevel);
        candidateData.put("otherLanguageLevel", otherLanguageLevel);
        candidateData.put("workExperienceYears", workExperienceYears);
        candidateData.put("officeSkills", officeSkills);
        candidateData.put("programmingLanguage", programmingLanguage);

        candidateCounter++;
        String candidateKey = "cand" + candidateCounter;
        candidates.put(candidateKey, candidateData);
        
        return candidates;
    }

public List<String> getFullNames() {
    List<String> names = new ArrayList<>();
    String outputFilePath = "E:\\myCVision\\mycv\\src\\resources\\cv\\output.json";
    
    try {
        // Διάβασε το JSON αρχείο
        String jsonContent = Files.readString(Path.of(outputFilePath), StandardCharsets.UTF_8);
        Gson gson = new Gson();
        
        // Μετατροπή JSON σε Map
        Type type = new TypeToken<Map<String, Map<String, Object>>>(){}.getType();
        Map<String, Map<String, Object>> candidatesData = gson.fromJson(jsonContent, type);
        
        // Εξαγωγή των ονομάτων
        for (Map.Entry<String, Map<String, Object>> entry : candidatesData.entrySet()) {
            String fullName = (String) entry.getValue().get("fullName");
            if (fullName != null && !fullName.trim().isEmpty()) {
                names.add(fullName);
            }
        }
    } catch (IOException e) {
        System.err.println("Σφάλμα κατά την ανάγνωση του αρχείου JSON: " + e.getMessage());
        e.printStackTrace();
    }
    
    return names;
}
}   